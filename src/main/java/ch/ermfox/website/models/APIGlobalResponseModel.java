package ch.ermfox.website.models;

import java.util.Map;

public class APIGlobalResponseModel<T> {
    private String navbarTitle = "My Portfolio Site";
    private Map<String, Object> footer;
    private T data;

    public APIGlobalResponseModel(T data) {
        this.data = data;
        this.footer = Map.of(
                "connect", Map.of(
                        "github", "github",
                        "github_url", "https://github.com/ermfox",
                        "twitter", "twitter",
                        "twitter_url","https://x.com/ERMFoxGMG",
                        "linkedin", "linkedin",
                        "linkedin_url","https://www.linkedin.com/in/sophie-ahmed-4514b2332/",
                        "mail","mailto:sophie.saa@pm.me"
                )
        );
    }

    // getters
    public String getNavbarTitle() { return navbarTitle; }
    public Map<String, Object> getFooter() { return footer; }
    public T getData() { return data; }
}
