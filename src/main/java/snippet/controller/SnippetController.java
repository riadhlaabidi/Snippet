package snippet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import snippet.model.Snippet;
import snippet.persistence.service.SnippetService;

import java.util.UUID;

@Controller
public class SnippetController {

    private final SnippetService snippetService;

    @Autowired
    public SnippetController(SnippetService snippetService) {
       this.snippetService = snippetService;
    }

    @GetMapping("/new")
    public String newCode() {
        return "create";
    }

    @GetMapping("/{uuid}")
    public String getCode(@PathVariable final UUID uuid, final Model model) {
        final Snippet snippet = snippetService.get(uuid);
        if (snippet.isRestrictedByTime() || snippet.isRestrictedByViews()) {
            snippetService.updateTimeAndViews(snippet);
        }
        model.addAttribute("snippet", snippet);
        return "snippet";
    }

    @GetMapping("/latest")
    public String latest(final Model model) {
        model.addAttribute("list", snippetService.latestSnippets());
        return "latest";
    }
}