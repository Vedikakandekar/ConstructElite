package com.constructElite.controller;

import com.constructElite.Entity.*;
import com.constructElite.Services.*;
import com.constructElite.helper.UserMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("sp/")
public class SpController {

    @Autowired
    UserService userService;

    @Autowired
    UserDocumentsService userDocService;

    @Autowired
    RequestService requestService;


    @Autowired
    ContractDetailsService contractDetailsService;

    @Autowired
    ProjectService projectService;

    @Autowired
    MilestonesService milestonesService;


    private ModelAndView setupSPDashboardModel() {
        ModelAndView mav = new ModelAndView();
        User currUser = userService.getCurrentLoggedInUser();
        mav.addObject("CurrentUser", currUser);
        mav.addObject("title", "Welcome " + currUser.getName());
        mav.setViewName("/sp/spDashboard");
        return mav;
    }


    @GetMapping("/spDashboard")
    public ModelAndView baseDash() {
        return setupSPDashboardModel();

    }

    @GetMapping("/spDocumentDetails")
    public ModelAndView spDocDetails() {
        ModelAndView m = new ModelAndView();
        User currUser = userService.getCurrentLoggedInUser();
        List<UserDocuments> userDocumentsList = userDocService.getUserDocumentWithoutContaint(currUser);
        UserDocuments license = userDocService.getUserDocByClientAndDocName(currUser, "license");
        if (license == null) {
            m.addObject("licenseDoc", true);
        } else {
            m.addObject("licenseDoc", false);
        }
        m.addObject("title", "Your Documents");
        m.addObject("userDocuments", userDocumentsList);
        m.addObject("CurrentUser", currUser);
        m.setViewName("/sp/spDocumentDetails");
        return m;
    }

    @GetMapping(value = "/download/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> fetchDocument(@PathVariable int id) {
        UserDocuments document = userDocService.getUserDocumentById(id);

        if (document != null) {

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getDocumentName() + "\"")
                    .body(document.getDocument());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/uploadLiscence")
    public ModelAndView saveDocumentToDb(@RequestParam("license") MultipartFile license) {
        saveUserDoc(license, "license", userService.getCurrentLoggedInUser());
        ModelAndView m = new ModelAndView();
        User currUser = userService.getCurrentLoggedInUser();
        List<UserDocuments> userDocumentsList = userDocService.getUserDocumentWithoutContaint(currUser);
        m.addObject("title", "Your Documents");
        m.addObject("userDocuments", userDocumentsList);
        m.addObject("CurrentUser", currUser);
        m.setViewName("/sp/spDocumentDetails");
        m.addObject("licenseDoc", false);
        return m;

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


    @GetMapping("/docRequests")
    public ModelAndView getDocumentRequestsPage() {
        ModelAndView m = new ModelAndView();
        User currUser = userService.getCurrentLoggedInUser();
        List<Requests> requestsList = requestService.getRequestBySpId(currUser);
        m.addObject("requestsList", requestsList);
        m.addObject("CurrentUser", currUser);
        m.setViewName("/sp/docRequests");
        return m;
    }

    @PostMapping("/uploadReqDocument")
    public ModelAndView saveRequestDocumentToDb(@ModelAttribute Requests request,
                                                @RequestParam("document") MultipartFile document) {
        saveReqDoc(document, request);
        ModelAndView m = new ModelAndView();
        User currUser = userService.getCurrentLoggedInUser();
        List<Requests> requestsList = requestService.getRequestBySpId(currUser);
        m.addObject("requestsList", requestsList);
        m.addObject("CurrentUser", currUser);
        m.setViewName("/sp/docRequests");
        return m;
    }

    @GetMapping("/legalContractSentBySp/{requestId}")
    private ModelAndView legalContractSentBySp(@PathVariable("requestId") int requestId) {
        try {
            Requests requests = requestService.findByRequestId(requestId);
            if (requests.getRequestId() == requestId) {
                requests.setFulfilledAt(LocalDateTime.now());
                requests.setStatus(true);
                Requests savedReq = requestService.saveRequestToDb(requests);
                if (savedReq.getRequestId() > 0) {
                    ModelAndView m = new ModelAndView();
                    User currUser = userService.getCurrentLoggedInUser();
                    List<Requests> requestsList = requestService.getRequestBySpId(currUser);
                    m.addObject("requestsList", requestsList);
                    m.addObject("CurrentUser", currUser);
                    m.setViewName("/sp/docRequests");
                    return m;
                }
            }

            ModelAndView m = new ModelAndView();
            m.setViewName("/error");
            return m;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private void saveReqDoc(MultipartFile file, Requests req) {
        try {
            req.setFulfilledAt(LocalDateTime.now());
            req.setRequestedDoc(file.getBytes());
            requestService.saveRequestToDb(req);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/updateSpProfile")
    public ModelAndView updateSPDetails(@ModelAttribute("CurrentUser") User user) {
        ModelAndView m = new ModelAndView();

        userService.saveNewUserToDb(user);
        User currUser = userService.getCurrentLoggedInUser();
        m.addObject("CurrentUser", currUser);
        m.addObject("title", "Welcome " + currUser.getName());
        m.setViewName("/sp/spDashboard");
        return m;
    }

    @GetMapping(value = "/downloadContract/{requestId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> fetchContract(@PathVariable("requestId") int requestId) {
        byte[] doc = requestService.findRequestedDocumentByRequestId(requestId);
        if (doc != null) {

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Contract")
                    .body(doc);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping(value = "/AcceptValidateContractRequest/{requestId}")
    public ModelAndView AcceptValidateContractRequest(@PathVariable int requestId) {

        Requests currReq = requestService.findByRequestId(requestId);
        if (currReq != null) {
            currReq.setStatus(true);
            currReq.setFulfilledAt(LocalDateTime.now());
            Requests prevReq = requestService.saveRequestToDb(currReq);

            if (prevReq.getRequestId() > 0) {
                ContractDetails contract = new ContractDetails();
                contract.setValidated(true);
                contract.setRequestId(prevReq);
                contract.setSignedAt(LocalDateTime.now());
                contract.setStatus("In-Progress");

                ContractDetails result = contractDetailsService.saveContractToDb(contract);
                if (result.getContractId() > 0) {


                    ModelAndView m = new ModelAndView();
                    User currUser = userService.getCurrentLoggedInUser();
                    List<Requests> requestsList = requestService.getRequestBySpId(currUser);
                    m.addObject("requestsList", requestsList);
                    m.addObject("CurrentUser", currUser);
                    m.setViewName("/sp/docRequests");
                    return m;
                } else {

                    ModelAndView m = new ModelAndView();
                    m.setViewName("/error");
                    return m;
                }

            }

            ModelAndView m = new ModelAndView();
            m.setViewName("/error");
            return m;
        }

        ModelAndView m = new ModelAndView();
        m.setViewName("/error");
        return m;
    }

    @GetMapping("/rejectValidateContractRequest/{requestId}")
    public ModelAndView rejectDocRequest(@PathVariable int requestId) {

        Requests currReq = requestService.findByRequestId(requestId);
        if (currReq != null) {
            currReq.setStatus(false);
            currReq.setFulfilledAt(LocalDateTime.now());
            Requests prevReq = requestService.saveRequestToDb(currReq);
            if (prevReq.getRequestId() > 0) {
                ModelAndView m = new ModelAndView();
                User currUser = userService.getCurrentLoggedInUser();
                List<Requests> requestsList = requestService.getRequestBySpId(currUser);
                m.addObject("requestsList", requestsList);
                m.addObject("CurrentUser", currUser);
                m.setViewName("/sp/docRequests");
                return m;
            }


        }
        ModelAndView m = new ModelAndView();
        m.setViewName("/error");
        return m;

    }

    @GetMapping("/spProjects")
    public ModelAndView getSpProjects() {
        ModelAndView m = new ModelAndView();
        User currUser = userService.getCurrentLoggedInUser();
        List<Project> projectList = projectService.getProjectsBySpId(currUser.getUserId());
        m.addObject("projectList", projectList);
        m.addObject("CurrentUser", currUser);
        m.setViewName("/sp/spProjects");
        return m;
    }

    @GetMapping("/getProgressPage/{projectId}")
    public ModelAndView getProgressPage(@PathVariable("projectId") int projectId) {
        ModelAndView m = new ModelAndView();
        User currUser = userService.getCurrentLoggedInUser();
        Optional<Project> projectOptional = projectService.getProjectById(projectId);
        Project project;
        List<Milestones> milestonesList;
        if (projectOptional.isPresent()) {
            project=projectOptional.get();
            milestonesList = milestonesService.findByProject(project);
        }
        else {
            milestonesList = new ArrayList<Milestones>();
            project = new Project();
        }
        m.addObject("CurrentUser", currUser);
        m.addObject("milestonesList", milestonesList);
        System.out.println(project);
        m.addObject("project", project);
        m.setViewName("/sp/progressPage");
        return m;
    }

    @PostMapping("/add-new-milestone")
    public ModelAndView addNewMilestone(@ModelAttribute("milestones") Milestones milestones) {
        try {

                Milestones p = milestonesService.saveNewMilestoneToDb(milestones);
                if (p.getMilestoneId() > 0) {
                    ModelAndView m = new ModelAndView();
                    User currUser = userService.getCurrentLoggedInUser();
                    Optional<Project> projectOptional = projectService.getProjectById(milestones.getProjectId().getProjectId());
                    Project project;
                    List<Milestones> milestonesList;
                    if (projectOptional.isPresent()) {
                        project=projectOptional.get();
                        milestonesList = milestonesService.findByProject(project);
                    }
                    else {
                        milestonesList = new ArrayList<Milestones>();
                        project = new Project();
                    }
                    m.addObject("CurrentUser", currUser);
                    m.addObject("milestonesList", milestonesList);
                    System.out.println(project);
                    m.addObject("project", project);
                    m.setViewName("/sp/progressPage");
                    return m;

            }

        } catch (Exception e) {
            ModelAndView m = new ModelAndView();
            m.setViewName("/error");
            return m;

        }
        ModelAndView m = new ModelAndView();
        m.setViewName("/error");
        return m;
    }

}
