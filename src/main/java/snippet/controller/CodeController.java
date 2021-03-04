package snippet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import snippet.model.Code;
import snippet.exception.SnippetNotFoundException;
import snippet.repository.CodeRepository;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/code")
public class CodeController {

    private final CodeRepository codeRepository;

    @Autowired
    public CodeController(CodeRepository codeRepository) {
       this.codeRepository = codeRepository;
    }

    @GetMapping("/new")
    public String newCode() {
        return "create";
    }

    @GetMapping("/{uuid}")
    public String getCode(@PathVariable final UUID uuid, final Model model) {
        final Code code = codeRepository.findByUuid(uuid).orElseThrow(
                SnippetNotFoundException::new
        );
        if (code.isRestrictedByTime() || code.isRestrictedByViews()) {
            codeRepository.updateTimeAndViews(code);
        }
        model.addAttribute("code_obj", code);
        return "code";
    }

    @GetMapping("/latest")
    public String latest(final Model model) {
        List<Code> list = codeRepository.latestTenPublicSnippets();
        model.addAttribute("list", list);
        return "latest";
    }
}