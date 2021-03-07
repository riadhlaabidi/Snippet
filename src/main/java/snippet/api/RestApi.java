package snippet.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import snippet.model.Snippet;
import snippet.persistence.service.SnippetService;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api")
public class RestApi {

    private final SnippetService snippetService;

    @Autowired
    public RestApi(final SnippetService snippetService) {
        this.snippetService = snippetService;
    }

    @PostMapping(value = "/new", consumes = "application/json")
    public HashMap<String, UUID> newCode(@RequestBody final Snippet snippet) {
        snippet.setRestrictedByTime(snippet.getTime() != 0);
        snippet.setRestrictedByViews(snippet.getViews() != 0);
        snippetService.add(snippet);
        var out = new HashMap<String, UUID>();
        out.put("id", snippet.getUuid());
        return out;
    }

    @GetMapping("/{uuid}")
    public Snippet getCode(@PathVariable final UUID uuid) {
        Snippet snippet = snippetService.get(uuid);
        if (snippet.isRestrictedByTime() || snippet.isRestrictedByViews()) {
            snippetService.updateTimeAndViews(snippet);
        }
        return snippet;
    }

    @GetMapping("/latest")
    public List<Snippet> latest() {
        return snippetService.latestSnippets();
    }
}
