package ch.ermfox.website.github;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GitHubConfig {

    @Value("${github.token}")
    private String githubToken;

    public String getToken() {
        return githubToken;
    }
}
