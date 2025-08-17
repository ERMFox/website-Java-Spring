package ch.ermfox.website.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class GithubService {
    private final WebClient client;
    private final String username;

    public GithubService(
            @Value("${github.token}") String token,
            @Value("${github.username}") String username
    ) {
        this.username = username;
        this.client = WebClient.builder()
                .baseUrl("https://api.github.com/graphql")
                .defaultHeader("Authorization", "Bearer " + token)
                .build();
    }

    private static final ParameterizedTypeReference<Map<String, Object>> MAP =
            new ParameterizedTypeReference<>() {};

    // --- Queries ---
    private static final String QUERY_PINNED = """
      query($login: String!) {
        user(login: $login) {
          pinnedItems(first: 6, types: REPOSITORY) {
            nodes {
              ... on Repository {
                name
                description
                url
                primaryLanguage { name color }
                stargazerCount
                forkCount
                languages(first: 10) {
                  edges { size node { name color } }
                }
              }
            }
          }
        }
      }
      """;

    private static final String QUERY_ALL_PUBLIC = """
      query($login: String!, $pageSize: Int = 100, $cursor: String) {
        user(login: $login) {
          repositories(
            privacy: PUBLIC
            first: $pageSize
            after: $cursor
            ownerAffiliations: OWNER
            orderBy: { field: UPDATED_AT, direction: DESC }
          ) {
            totalCount
            pageInfo { hasNextPage endCursor }
            nodes {
              name
              description
              url
              isFork
              isArchived
              primaryLanguage { name color }
              stargazerCount
              forkCount
              languages(first: 10, orderBy: { field: SIZE, direction: DESC }) {
                edges { size node { name color } }
              }
            }
          }
        }
      }
      """;

    // --- Public API for controller ---
    public Mono<Map<String, Object>> runPinned() {
        return execute(QUERY_PINNED, Map.of("login", username));
    }

    public Mono<Map<String, Object>> runAllRepos(int pageSize, String cursor) {
        if (pageSize <= 0) pageSize = 100; // sane default

        final Map<String, Object> vars = new java.util.HashMap<>();
        vars.put("login", java.util.Objects.requireNonNull(username, "github.username is missing"));
        vars.put("pageSize", pageSize);
        if (cursor != null) vars.put("cursor", cursor); // Map.of() would NPE on null

        return execute(QUERY_ALL_PUBLIC, vars);
    }


    // --- Generic GraphQL executor ---
    private Mono<Map<String, Object>> execute(String query, Map<String, Object> variables) {
        return client.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("query", query, "variables", variables))
                .retrieve()
                .bodyToMono(MAP);
    }
}
