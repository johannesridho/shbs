package com.shbs.admin.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @GetMapping("")
    public String index() {
        return "admin/index";
    }

    @GetMapping("login")
    public String getLoginPage(Model model, @RequestParam Optional<String> error,
                               @RequestParam Optional<String> logout) {
        if (error.isPresent()) {
            model.addAttribute("error", error);
        } else if (logout.isPresent()) {
            model.addAttribute("logout", logout);
        }

        return "admin/login";
    }
}
