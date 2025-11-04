package ch.ermfox.website.repositories;

import ch.ermfox.website.models.ProjectDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectDetailsRepository extends JpaRepository<ProjectDetails, Long> {}
