package snippet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import snippet.model.Code;
import snippet.exception.CodeNotFoundException;
import snippet.repository.CodeRepository;

import java.util.List;
import java.util.UUID;

@Controller
public class CodeController {

    private final CodeRepository codeRepository;

    @Autowired
    public CodeController(CodeRepository codeRepository) {
       this.codeRepository = codeRepository;
    }

    @GetMapping("/code/new")
    public String newCode() {
        return "create";
    }

    @GetMapping("/code/{uuid}")
    public String getCode(@PathVariable UUID uuid, Model model) {
        final Code code = codeRepository.findByUuid(uuid).orElseThrow(
                CodeNotFoundException::new
        );
        if (code.isRestrictedByTime() || code.isRestrictedByViews()) {
            codeRepository.updateTimeAndViews(code);
        }
        model.addAttribute("code_obj", code);
        return "code";
    }

    @GetMapping("/code/latest")
    public String latest(Model model) {
        List<Code> list = codeRepository.latestTenPublicSnippets();
        model.addAttribute("list", list);
        return "latest";
    }
}