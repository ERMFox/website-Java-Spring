package ch.ermfox.website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebsiteApplication {

    public static void main(String[] args) {

        io.github.cdimascio.dotenv.Dotenv dotenv = io.github.cdimascio.dotenv.Dotenv.configure()
                .ignoreIfMissing()
                .load();
        System.setProperty("GITHUB_TOKEN", dotenv.get("GITHUB_TOKEN", ""));
        SpringApplication.run(WebsiteApplication.class, args);
    }

}
