package ch.ermfox.website.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MappingController {

    @GetMapping("/")
    String index(Model model) {
        model.addAttribute("title", "Website");
        return "index";
    }

    @GetMapping("/about")
    String about(Model model) {
        model.addAttribute("title", "About");
        return "about";
    }

    @GetMapping("/projects")
    String projects(Model model) {
        model.addAttribute("title", "Projects");
        return "projects";
    }

}
