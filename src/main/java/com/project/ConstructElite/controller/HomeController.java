package com.project.ConstructElite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String home(Model model)
    {
        model.addAttribute("title","Landing Page");
        return "home";
    }

    @RequestMapping("/signUp")
    public String signUp(Model model)
    {
        model.addAttribute("title","SignUp");
        return "signUp";
    }

    @RequestMapping("/login")
    public String login(Model model)
    {
        model.addAttribute("title","Login");
        return "login";
    }
}
