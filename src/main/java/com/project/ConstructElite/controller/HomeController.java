package com.project.ConstructElite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ConstructElite.Entities.User;
import com.project.ConstructElite.dao.UserRepository;
import com.project.ConstructElite.helper.UserMessage;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired
	private UserRepository userRepo;
	
//	Landing Page Mapping
    @RequestMapping("/")
    public String home(Model model)
    {
        model.addAttribute("title","Landing Page");
        return "home";
    }
    
    
    
//    Sign Up Page Mapping
    @RequestMapping("/signUp")
    public String signUp(Model model)
    {
        model.addAttribute("title","SignUp");
        model.addAttribute("user", new User());
        return "signUp";
    }
    
      
    
//   Registering New User
    @PostMapping("/registerUser")
    public String registerUser(@ModelAttribute("user") User user, Model model, HttpSession session)
    {
    	try {
    		
    		User already = this.userRepo.findByUserEmail(user.getUserEmail());
    		if(already!=null)
    		{
    			 model.addAttribute("user", user);
    	            session.setAttribute("message", new UserMessage("User with this email already exists", "alert-danger"));
    	            return "signUp";
    			
    		}
    	
    	User u = this.userRepo.save(user);
    	model.addAttribute("user", new User());
    	session.setAttribute("message", new UserMessage("User Registered Successfully", "alert-success"));
    	
    	return "login";
    	
    	}
    	catch (Exception e) {
			model.addAttribute("user", user);
			session.setAttribute("message", new UserMessage(e.getMessage(),"alert-danger"));
    		return "signUp";
		}
    	
    }
    
    
    
    
    
}
