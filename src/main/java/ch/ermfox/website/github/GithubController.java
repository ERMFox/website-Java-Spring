package ch.ermfox.website.github;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/github")
public class GithubController {
    private final GithubService service;
    public GithubController(GithubService service) { this.service = service; }

    @GetMapping("/pinned")
    public Mono<ResponseEntity<Map<String,Object>>> pinned() {
        return service.getPinned().map(ResponseEntity::ok);
    }
}
