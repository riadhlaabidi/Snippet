package snippet.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import snippet.model.Code;
import snippet.exception.SnippetNotFoundException;
import snippet.repository.CodeRepository;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/code")
public class RestApi {

    private final CodeRepository codeRepository;

    @Autowired
    public RestApi(final CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    @PostMapping(value = "/new", consumes = "application/json")
    public HashMap<String, UUID> newCode(@RequestBody final Code code) {
        code.setRestrictedByTime(code.getTime() != 0);
        code.setRestrictedByViews(code.getViews() != 0);
        var out = new HashMap<String, UUID>();
        out.put("id", codeRepository.save(code).getUuid());
        return out;
    }

    @GetMapping("/{uuid}")
    public Code getCode(@PathVariable final UUID uuid) {
        final Code code = codeRepository.findByUuid(uuid).orElseThrow(
                SnippetNotFoundException::new
        );
        if (code.isRestrictedByTime() || code.isRestrictedByViews()) {
            codeRepository.updateTimeAndViews(code);
        }
        return code;
    }

    @GetMapping("/latest")
    public List<Code> latest() {
        return codeRepository.latestTenPublicSnippets();
    }
}
