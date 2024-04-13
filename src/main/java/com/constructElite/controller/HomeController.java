package com.constructElite.controller;

import com.constructElite.Entity.User;
import com.constructElite.Entity.UserDocuments;
import com.constructElite.Services.UserDocumentsService;
import com.constructElite.Services.UserService;
import com.constructElite.helper.UserMessage;
import com.constructElite.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Controller
public class HomeController {
    @Autowired
    private final UserService userService;

    @Autowired
    private final UserDocumentsService userDocService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public HomeController(UserService userService, UserDocumentsService userDocService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userDocService = userDocService;
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
    public String registerUser(@Valid
                               @ModelAttribute("user") User user,
                               BindingResult result,
                               Model model,
                               @RequestParam("adhar") MultipartFile adhar,
                               @RequestParam("pan") MultipartFile pan,
                               HttpSession session) {
        try {
            if(result.hasErrors())
            {
                model.addAttribute("user",user);
                return "signUp";
            }

            User already = userService.findByEmail(user.getEmail());
            if (already != null) {
                model.addAttribute("user", user);
                session.setAttribute("message", new UserMessage("User with this email already exists", "alert-danger"));
                return "signUp";
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if (user.getRole().equals("ROLE_CLIENT"))
                user.setIsApproved(true);
            User u = userService.saveNewUserToDb(user);
            if (u.getUserId() > 0) {
                saveUserDoc(adhar, "adhar", u);
                saveUserDoc(pan, "pan", u);
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


    private void saveUserDoc(MultipartFile file, String name, User currUser) {
        try {
            UserDocuments userDoc1 = new UserDocuments();
            userDoc1.setType(file.getContentType());
            userDoc1.setDocumentName(name);
            userDoc1.setAddedAt(LocalDateTime.now());
            userDoc1.setUserId(currUser);
            userDoc1.setDocument(file.getBytes());

            userDocService.saveUserDocToDb(userDoc1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}