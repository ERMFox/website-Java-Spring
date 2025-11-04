package ch.ermfox.website.controllers;


import ch.ermfox.website.models.Project;
import ch.ermfox.website.services.SkillsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MappingController {

    private final SkillsService skillsService;

    public MappingController(SkillsService skillsService) {
        this.skillsService = skillsService;
    }

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

    @GetMapping("/skills")
    String skills(Model model) {
        model.addAttribute("title", "Skills");
        model.addAttribute("projects", skillsService.getProjects());
        return "skills";
    }

    @GetMapping("/skills/{slug}")
    String skillDetails(@PathVariable String slug, Model model) {
        Project project = skillsService.getProjectBySlug(slug);
        if (project == null) {
            return "error/404";
        }

        model.addAttribute("title", project.getTitle());
        model.addAttribute("project", project);

        String html = "";
        if (project.getDetails() != null && project.getDetails().getMarkdownContent() != null) {
            html = skillsService.renderMarkdown(project.getDetails().getMarkdownContent());
        }
        model.addAttribute("content", html);

        return "project-detail";
    }



}
