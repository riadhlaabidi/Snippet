package snippet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import snippet.model.Code;
import snippet.repository.SnippetService;

import java.util.UUID;

@Controller
@RequestMapping("/code")
public class CodeController {

    private final SnippetService snippetService;

    @Autowired
    public CodeController(SnippetService snippetService) {
       this.snippetService = snippetService;
    }

    @GetMapping("/new")
    public String newCode() {
        return "create";
    }

    @GetMapping("/{uuid}")
    public String getCode(@PathVariable final UUID uuid, final Model model) {
        final Code snippet = snippetService.get(uuid);
        if (snippet.isRestrictedByTime() || snippet.isRestrictedByViews()) {
            snippetService.updateTimeAndViews(snippet);
        }
        model.addAttribute("snippet", snippet);
        return "code";
    }

    @GetMapping("/latest")
    public String latest(final Model model) {
        model.addAttribute("list", snippetService.latestSnippets());
        return "latest";
    }
}