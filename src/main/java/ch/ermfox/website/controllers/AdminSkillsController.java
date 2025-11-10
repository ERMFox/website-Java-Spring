package ch.ermfox.website.controllers;

import ch.ermfox.website.models.Project;
import ch.ermfox.website.models.ProjectDetails;
import ch.ermfox.website.repositories.ProjectRepository;
import ch.ermfox.website.repositories.ProjectDetailsRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/skills")
public class AdminSkillsController {

    private final ProjectRepository projects;
    private final ProjectDetailsRepository detailsRepo;

    public AdminSkillsController(ProjectRepository projects, ProjectDetailsRepository detailsRepo) {
        this.projects = projects;
        this.detailsRepo = detailsRepo;
    }

    // LIST
    @GetMapping
    public String list(Model model) {
        model.addAttribute("projects", projects.findAll());
        model.addAttribute("title", "Manage Projects");
        return "admin/skills/list";   // templates/admin/skills/list.html
    }

    // CREATE — show form
    @GetMapping("/new")
    public String createForm(Model model) {

        Project p = new Project();
        model.addAttribute("project", p);
        model.addAttribute("markdown", "");
        model.addAttribute("title", "Create Project");

        // ✅ explicit form destination
        model.addAttribute("action", "/admin/skills/new");

        return "admin/skills/form";
    }

    // CREATE — submit
    @PostMapping("/new")
    public String createSubmit(
            @ModelAttribute Project project,
            @RequestParam("markdown") String markdown
    ) {
        ProjectDetails details = new ProjectDetails();
        details.setMarkdownContent(markdown);
        project.setDetails(details);

        projects.save(project);
        return "redirect:/admin/skills";
    }

    // EDIT — show form
    @GetMapping("/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        var project = projects.findById(id).orElse(null);
        if (project == null) return "error/404";

        String md = (project.getDetails() != null)
                ? project.getDetails().getMarkdownContent()
                : "";

        model.addAttribute("project", project);
        model.addAttribute("markdown", md);
        model.addAttribute("title", "Edit Project");

        // ✅ explicit edit submit destination
        model.addAttribute("action", "/admin/skills/" + id);

        return "admin/skills/form";
    }

    // EDIT — submit
    @PostMapping("/{id}")
    public String editSubmit(
            @PathVariable Long id,
            @ModelAttribute Project edited,
            @RequestParam("markdown") String markdown
    ) {
        var existing = projects.findById(id).orElse(null);
        if (existing == null) return "error/404";

        existing.setSlug(edited.getSlug());
        existing.setTitle(edited.getTitle());
        existing.setSummary(edited.getSummary());
        existing.setTechTags(edited.getTechTags());
        existing.setThumbnailUrl(edited.getThumbnailUrl());

        if (existing.getDetails() == null) {
            existing.setDetails(new ProjectDetails());
        }
        existing.getDetails().setMarkdownContent(markdown);

        projects.save(existing);
        return "redirect:/admin/skills";
    }

    // DELETE
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        projects.deleteById(id);
        return "redirect:/admin/skills";
    }
}
