package ch.ermfox.website.github;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/github")
public class GithubController {
    private final GithubService service;

    public GithubController(GithubService service) {
        this.service = service;
    }

    /** Get pinned repositories (max 6) */
    @GetMapping("/pinned")
    public Mono<ResponseEntity<Map<String, Object>>> pinned() {
        return service.runPinned()
                .map(ResponseEntity::ok);
    }

    /** Get all public repositories, paginated (default 100 per page) */
    @GetMapping("/repos")
    public Mono<ResponseEntity<Map<String, Object>>> repos(
            @RequestParam(defaultValue = "100") int pageSize,
            @RequestParam(required = false) String cursor
    ) {
        return service.runAllRepos(pageSize, cursor)
                .map(ResponseEntity::ok);
    }
}
