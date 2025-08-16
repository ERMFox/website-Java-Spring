package ch.ermfox.website.models;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

@ControllerAdvice
public class ThymeleafGlobalModel {

    @ModelAttribute("navbarTitle")
    public String navbarTitle() {
        return "Sophie (ERMFox) Portfolio";
    }

    @ModelAttribute("footer")
    public Map<String, Object> footer() {
        return Map.of(
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
}
