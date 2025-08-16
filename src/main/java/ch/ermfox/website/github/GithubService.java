package ch.ermfox.website.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.core.ParameterizedTypeReference;

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

    private static final String QUERY = """
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


    public Mono<Map<String, Object>> getPinned() {
        return client.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "query", QUERY,
                        "variables", Map.of("login", username)
                ))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

}
