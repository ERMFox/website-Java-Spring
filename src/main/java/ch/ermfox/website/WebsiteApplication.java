package ch.ermfox.website;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebsiteApplication {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        System.setProperty("github.token", dotenv.get("GITHUB_TOKEN", ""));
        System.setProperty("github.username", dotenv.get("GITHUB_USERNAME", "ermfox"));
        SpringApplication.run(WebsiteApplication.class, args);

        SpringApplication.run(WebsiteApplication.class, args);
    }

}
