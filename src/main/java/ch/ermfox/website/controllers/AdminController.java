package ch.ermfox.website.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String adminHome() {
        return "admin/home"; // templates/admin/home.html
    }

    @GetMapping("/admin/login")
    public String login() {
        return "admin/login"; // templates/admin/login.html
    }
}
