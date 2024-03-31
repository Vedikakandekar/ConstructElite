package com.constructElite.controller;

import com.constructElite.Entity.User;
import com.constructElite.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("admin/")
public class AdminController {

    @Autowired
    UserService userService;

    @GetMapping("/adminDashboard")
    public String baseDash(Model model) {
        model.addAttribute("title", "Dashboard");
        return "admin/adminDashboard";
    }

    @GetMapping("/getNewSp")
    public ModelAndView getAllNewSp()
    {
        List<User> list = userService.getNewSP();
		ModelAndView m=new ModelAndView();
		m.setViewName("admin/adminDashboard");
		m.addObject("userList",list);
        m.addObject("title","New Service Providers");
        return m;
    }

    @GetMapping("/getApprovedSP")
    public ModelAndView getApprovedSp()
    {
        List<User> list = userService.getApprovedSP();
        ModelAndView m=new ModelAndView();
        m.setViewName("admin/adminDashboard");
        m.addObject("userList",list);
        m.addObject("clients",false);
        m.addObject("title","Approved Service Providers");
        return m;
    }

    @GetMapping("/getDisapprovedSp")
    public ModelAndView getDisapprovedSp()
    {
        List<User> list = userService.getDisapprovedSP();
        ModelAndView m=new ModelAndView();
        m.setViewName("admin/adminDashboard");
        m.addObject("userList",list);
        m.addObject("title","Disapproved Service Providers");
        return m;
    }

    @GetMapping("/getAllClients")
    public ModelAndView getAllClients()
    {
        List<User> list = userService.findUsersByRoleClient();
        ModelAndView m=new ModelAndView();
        m.setViewName("admin/adminDashboard");
        m.addObject("userList",list);
        m.addObject("clients",true);
        m.addObject("title","All Clients");
        return m;
    }




}
