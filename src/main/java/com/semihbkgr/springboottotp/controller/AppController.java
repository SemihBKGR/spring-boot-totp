package com.semihbkgr.springboottotp.controller;

import com.semihbkgr.springboottotp.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class AppController {

    private final UserService userService;

    @GetMapping

    public String index() {
        return "index";
    }

    @GetMapping("/signin")
    public String signin() {
        return "signin";
    }

    @GetMapping("/profile")
    public String profile(HttpServletRequest request, Model model) {
        var username = request.getUserPrincipal().getName();
        var user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "profile";
    }

}
