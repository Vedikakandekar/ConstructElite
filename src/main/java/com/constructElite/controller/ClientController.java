package com.constructElite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("client/")
public class ClientController {

    @GetMapping("clientDashboard")
    public String baseDash(Model model) {
        model.addAttribute("title", "Dashboard");
        return "client/ClientDashboard";
    }
}
