package com.constructElite.controller;

import com.constructElite.Entity.User;
import com.constructElite.Services.UserService;
import com.constructElite.config.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("admin/")
public class AdminController {

    @Autowired
    UserService userService;

    private ModelAndView setupAdminDashboardModel() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("CurrentUser", userService.getCurrentLoggedInUser());
        mav.setViewName("/admin/adminDashboard");
        return mav;
    }

    @GetMapping("/searchAll")
    public ModelAndView searchAll(@RequestParam String searchTerm)
    {
        ModelAndView m = setupAdminDashboardModel();
        List<User> userList = userService.searchAllUsers(searchTerm);
        for(User u : userList)
            System.out.println(u);
        m.addObject("title", "Search Results");
        m.addObject("userList", userList);
        return m;
    }

    @GetMapping("/adminDashboard")
    public ModelAndView baseDash(Model model) {
        List<User> list = userService.getNewSP();
        ModelAndView m=setupAdminDashboardModel();

        m.addObject("userList",list);
        m.addObject("title","New Service Providers");
        return m;
    }

    @GetMapping("/getNewSp")
    public ModelAndView getAllNewSp()
    {
        List<User> list = userService.getNewSP();
		ModelAndView m=setupAdminDashboardModel();

		m.addObject("userList",list);
        m.addObject("title","New Service Providers");
        return m;
    }

    @GetMapping("/getApprovedSP")
    public ModelAndView getApprovedSp()
    {
        List<User> list = userService.getApprovedSP();
        ModelAndView m=setupAdminDashboardModel();
        m.addObject("userList",list);
        m.addObject("clients",false);
        m.addObject("title","Approved Service Providers");
        return m;
    }

    @GetMapping("/getDisapprovedSp")
    public ModelAndView getDisapprovedSp()
    {
        List<User> list = userService.getDisapprovedSP();
        ModelAndView m=setupAdminDashboardModel();
        m.addObject("userList",list);
        m.addObject("title","Disapproved Service Providers");
        return m;
    }

    @GetMapping("/getAllClients")
    public ModelAndView getAllClients()
    {
        List<User> list = userService.findUsersByRoleClient();
        ModelAndView m=setupAdminDashboardModel();

        m.addObject("userList",list);
        m.addObject("clients",true);
        m.addObject("title","All Clients");
        return m;
    }

    @GetMapping("/approve-sp/{userId}")
    public ModelAndView approveSpId(@PathVariable("userId") int userId)
    {
        Optional<User> u = userService.getUserById(userId);
        if(u.isPresent())
        {
            User user = u.get();
            user.setIsApproved(true);
            userService.saveNewUserToDb(user);
            ModelAndView m= setupAdminDashboardModel();
            List<User> list = userService.getApprovedSP();
            m.addObject("userList",list);
            m.addObject("clients",false);
            m.addObject("title","Approved Service Providers");
            return m;
        }
        else
            return new ModelAndView("/error");
    }

    @GetMapping("/disapprove-sp/{userId}")
    public ModelAndView disapproveSpId(@PathVariable("userId") int userId)
    {
        Optional<User> u = userService.getUserById(userId);
        if(u.isPresent())
        {
            User user = u.get();
            user.setIsApproved(false);
            userService.saveNewUserToDb(user);
            List<User> list = userService.getDisapprovedSP();
            ModelAndView m=setupAdminDashboardModel();
            m.addObject("userList",list);
            m.addObject("title","Disapproved Service Providers");
            return m;
        }
        else
            return new ModelAndView("/error");
    }


}
