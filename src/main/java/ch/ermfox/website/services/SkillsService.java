package ch.ermfox.website.services;

import ch.ermfox.website.models.Project;
import ch.ermfox.website.repositories.ProjectRepository;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.springframework.stereotype.Service;
import com.vladsch.flexmark.util.ast.Node;

import java.util.List;

@Service
public class SkillsService {

    private final ProjectRepository projectRepository;
    private final Parser parser;
    private final HtmlRenderer renderer;

    public SkillsService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;

        MutableDataSet opts = new MutableDataSet();
        this.parser = Parser.builder(opts).build();
        this.renderer = HtmlRenderer.builder(opts).build();
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectBySlug(String slug) {
        return projectRepository.findBySlug(slug).orElse(null);
    }

    public String renderMarkdown(String markdown) {
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }
}
