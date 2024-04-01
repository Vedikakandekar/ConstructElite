package com.constructElite.controller;

import com.constructElite.Entity.User;
import com.constructElite.Services.UserService;
import com.constructElite.helper.UserMessage;
import com.constructElite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
    @Autowired
    private final UserService userService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public HomeController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    //	Landing Page Mapping
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Landing Page");
        return "home";
    }

    @GetMapping("/signIn")
    public String signIn(Model model) {
        model.addAttribute("title", "SignIn Page");
        return "signIn";
    }


    //    Sign Up Page Mapping
    @GetMapping("/signUp")
    public String signUp(Model model) {
        model.addAttribute("title", "SignUp");
        model.addAttribute("user", new User());
        return "signUp";
    }


    @PostMapping("/registerUser")
    public String registerUser(@ModelAttribute("user") User user, Model model, HttpSession session) {
        try {
            User already = userService.findByEmail(user.getEmail());
            if (already != null) {
                model.addAttribute("user", user);
                session.setAttribute("message", new UserMessage("User with this email already exists", "alert-danger"));
                return "signUp";
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if(user.getRole().equals("ROLE_CLIENT"))
                user.setIsApproved(true);
            User u = userService.saveNewUserToDb(user);
            if (u.getUserId() > 0) {

                return "signIn";
            } else {
                return "signUp";
            }
        } catch (Exception e) {
            model.addAttribute("user", user);
            session.setAttribute("message", new UserMessage(e.getMessage(), "alert-danger"));
            return "signUp";
        }
    }






}