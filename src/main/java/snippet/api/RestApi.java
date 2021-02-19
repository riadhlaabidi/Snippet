package snippet.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import snippet.model.Code;
import snippet.exception.CodeNotFoundException;
import snippet.repository.CodeRepository;

import java.util.List;
import java.util.UUID;


@RestController
public class RestApi {

    private final CodeRepository codeRepository;

    @Autowired
    public RestApi(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    @PostMapping(value = "/api/code/new", consumes = "application/json")
    public String newCode(@RequestBody Code code) {
        code.setRestrictedByTime(code.getTime() != 0);
        code.setRestrictedByViews(code.getViews() != 0);
        return String.format("{ \"id\": \"%s\" }", codeRepository.save(code).getUuid());
    }

    @GetMapping("/api/code/{uuid}")
    public Code getCode(@PathVariable UUID uuid) {
        final Code code = codeRepository.findByUuid(uuid).orElseThrow(
                CodeNotFoundException::new
        );
        if (code.isRestrictedByTime() || code.isRestrictedByViews()) {
            codeRepository.updateTimeAndViews(code);
        }
        return code;
    }

    @GetMapping("/api/code/latest")
    public List<Code> latest() {
        return codeRepository.latestTenPublicSnippets();
    }
}
