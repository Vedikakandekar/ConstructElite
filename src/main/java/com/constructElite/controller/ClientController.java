package com.constructElite.controller;

import com.constructElite.Entity.Project;
import com.constructElite.Entity.User;
import com.constructElite.Services.ProjectService;
import com.constructElite.Services.UserService;
import com.constructElite.helper.UserMessage;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.ServerSocket;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("client/")
public class ClientController {

    @Autowired
    ProjectService projectService;

    @Autowired
    UserService userService;

    private ModelAndView setupClientDashboardModel() {
        ModelAndView m = new ModelAndView();
        m.addObject("CurrentUser", userService.getCurrentLoggedInUser());
        return m;
    }

    @GetMapping("/get-all-projects/{currUid}")
    public ModelAndView getAllProjects(@PathVariable("currUid") int currUid)
    {
        ModelAndView m =new ModelAndView();
        m.addObject("CurrentUser", userService.getCurrentLoggedInUser());

        List<Project> projectList = projectService.getProjectsByClientId(currUid);

        System.out.println();
        System.out.println(projectList);
        System.out.println();

        m.setViewName("/client/spDetails");
        m.addObject("projectList",projectList);
        return m;
    }

    @GetMapping("/search")
    public ModelAndView searchUsers(@RequestParam String searchTerm) {
        List<User> userList = userService.searchSPUsers(searchTerm);
        ModelAndView m =new ModelAndView();
        m.addObject("CurrentUser", userService.getCurrentLoggedInUser());
        m.addObject("title","Search Results");
        m.setViewName("client/clientDashboard");
        m.addObject("userList", userList);
        return m;
    }

    @GetMapping("/clientDashboard")
    public ModelAndView baseDash() {

        ModelAndView m =new ModelAndView();
        m.addObject("CurrentUser", userService.getCurrentLoggedInUser());
        m.addObject("title", "Dashboard");
        m.setViewName("client/clientDashboard");
        return m;
    }


    @GetMapping("/create-new-project")
    public ModelAndView creatNewProject()
    {
        ModelAndView m =new ModelAndView();
        m.addObject("CurrentUser", userService.getCurrentLoggedInUser());
        m.addObject("title","Create New Project");
        m.setViewName("client/createNewProject");
        return m;
    }

    @PostMapping("/add-new-project")
    public ModelAndView addNewProject(@ModelAttribute("project") Project project, HttpSession session)
    {

        ModelAndView m =new ModelAndView();
        m.addObject("CurrentUser", userService.getCurrentLoggedInUser());
        try
        {

            Project already = projectService.getProjectByName(project.getName());
            if(already!=null)
            {
                m.addObject("project",project);
                m.setViewName("client/createNewProject");
                session.setAttribute("message", new UserMessage("Project with this Name already exists, Try Using another Name !!", "alert-danger"));
                return m;

            }
            project.setClientOnProject(userService.getCurrentLoggedInUser());
            project.setCreatedAt(LocalDateTime.now());
            Project p =projectService.saveNewProjectToDb(project);
            if(p.getProjectId()>0)
            {
                m.setViewName("client/clientDashboard");
                return m;
            }
            else {
                m.addObject("project",project);
                m.setViewName("client/createNewProject");
                session.setAttribute("message", new UserMessage("Something went wrong with Database, Try again Later !!!", "alert-warning"));
                return m;

            }
        }catch (Exception e)
        {
            m.addObject("project",project);
            m.setViewName("client/createNewProject");
            session.setAttribute("message", new UserMessage("Something went wrong, Try again Later !!!", "alert-danger"));
            return m;

        }
    }


}
