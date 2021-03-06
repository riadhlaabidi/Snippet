package snippet.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import snippet.model.Code;
import snippet.exception.SnippetNotFoundException;
import snippet.repository.CodeRepository;
import snippet.repository.SnippetService;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/code")
public class RestApi {

    private final SnippetService snippetService;

    @Autowired
    public RestApi(final SnippetService snippetService) {
        this.snippetService = snippetService;
    }

    @PostMapping(value = "/new", consumes = "application/json")
    public HashMap<String, UUID> newCode(@RequestBody final Code code) {
        code.setRestrictedByTime(code.getTime() != 0);
        code.setRestrictedByViews(code.getViews() != 0);
        snippetService.add(code);
        var out = new HashMap<String, UUID>();
        out.put("id", code.getUuid());
        return out;
    }

    @GetMapping("/{uuid}")
    public Code getCode(@PathVariable final UUID uuid) {
        Code snippet = snippetService.get(uuid);
        if (snippet.isRestrictedByTime() || snippet.isRestrictedByViews()) {
            snippetService.updateTimeAndViews(snippet);
        }
        return snippet;
    }

    @GetMapping("/latest")
    public List<Code> latest() {
        return snippetService.latestSnippets();
    }
}
