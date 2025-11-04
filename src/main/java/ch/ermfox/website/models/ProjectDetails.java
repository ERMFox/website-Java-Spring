package ch.ermfox.website.models;

import jakarta.persistence.*;

@Entity
@Table(name = "project_details")
public class ProjectDetails {

    protected ProjectDetails() {}


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "LONGTEXT")
    private String markdownContent;

    public Long getId() { return id; }
    public String getMarkdownContent() { return markdownContent; }
    public void setMarkdownContent(String markdownContent) { this.markdownContent = markdownContent; }
}
