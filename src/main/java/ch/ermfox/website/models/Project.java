package ch.ermfox.website.models;

import jakarta.persistence.*;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String slug;

    private String title;

    @Column(length = 1000)
    private String summary;

    private String techTags;
    private String thumbnailUrl;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "details_id")
    private ProjectDetails details;


    public Project() {}

    // Getters/setters

    public Long getId() { return id; }
    public String getSlug() { return slug; }
    public String getTitle() { return title; }
    public String getSummary() { return summary; }
    public String getTechTags() { return techTags; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public ProjectDetails getDetails() { return details; }

    public void setSlug(String slug) { this.slug = slug; }
    public void setTitle(String title) { this.title = title; }
    public void setSummary(String summary) { this.summary = summary; }
    public void setTechTags(String techTags) { this.techTags = techTags; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public void setDetails(ProjectDetails details) { this.details = details; }
}
