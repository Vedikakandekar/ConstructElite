package com.constructElite.controller;

import com.constructElite.Entity.*;
import com.constructElite.Services.*;
import com.constructElite.helper.UserMessage;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;


@Controller
@RequestMapping("client/")
public class ClientController {

    @Autowired
    ProjectService projectService;

    @Autowired
    UserService userService;

    @Autowired
    RequestService requestService;

    @Autowired
    ProjectDocumentsService pDocService;

    @Autowired
    ContractDetailsService contractService;

    private ModelAndView setupClientDashboardModel() {
        List<User> userList = userService.getAllSp();
        ModelAndView m = new ModelAndView();
        m.addObject("CurrentUser", userService.getCurrentLoggedInUser());
        m.addObject("title", "Service Providers");
        m.setViewName("/client/clientDashboard");
        m.addObject("userList", userList);

        return m;
    }

    private ModelAndView setupClientProjectModel(int currUid) {
        ModelAndView m = new ModelAndView();
        m.addObject("CurrentUser", userService.getCurrentLoggedInUser());

        List<Project> projectList = projectService.getProjectsByClientId(currUid);
        m.addObject("title", "Projects");
        m.setViewName("/client/userProjects");
        m.addObject("projectList", projectList);
        return m;
    }

    @GetMapping("/get-all-projects/{currUid}")
    public ModelAndView getAllProjects(@PathVariable("currUid") int currUid) {
        ModelAndView m = setupClientProjectModel(currUid);

        return m;

    }

    @GetMapping("/searchSp")
    public ModelAndView searchUsers(@RequestParam String searchTerm) {
        List<User> userList = userService.searchSPUsers(searchTerm);
        ModelAndView m = new ModelAndView();
        m.addObject("CurrentUser", userService.getCurrentLoggedInUser());
        m.addObject("title", "Search Results");
        m.setViewName("client/clientDashboard");
        m.addObject("userList", userList);
        return m;
    }

    @GetMapping("/clientDashboard")
    public ModelAndView baseDash() {

        ModelAndView m = setupClientDashboardModel();
        return m;
    }

    @PostMapping("/add-new-project")
    public String addNewProject(@Valid @ModelAttribute("project") Project project,
                                BindingResult result, Model model, HttpSession session) {
        try {
            User currUser = userService.getCurrentLoggedInUser();

            model.addAttribute("CurrentUser", currUser);
            if (result.hasErrors()) {
                model.addAttribute("project", project);
                return "client/createNewProject";
            }

            Project already = projectService.getProjectByName(project.getName());
            if (already != null) {
                model.addAttribute("project", project);
                session.setAttribute("message", new UserMessage("Project with this Name already exists, Try Using another Name !!", "alert-danger"));
                return "client/createNewProject";

            }
            project.setClientOnProject(userService.getCurrentLoggedInUser());
            project.setCreatedAt(LocalDateTime.now());
            Project p = projectService.saveNewProjectToDb(project);
            if (p.getProjectId() > 0) {
                model.addAttribute("CurrentUser", userService.getCurrentLoggedInUser());

                List<Project> projectList = projectService.getProjectsByClientId(currUser.getUserId());
                model.addAttribute("title", "Projects");
                model.addAttribute("projectList", projectList);
                return "/client/userProjects";
            } else {
                model.addAttribute("project", project);

                session.setAttribute("message", new UserMessage("Something went wrong with Database, Try again Later !!!", "alert-warning"));
                return "client/createNewProject";

            }
        } catch (Exception e) {
            model.addAttribute("project", project);

            session.setAttribute("message", new UserMessage("Something went wrong, Try again Later !!!", "alert-danger"));
            return "client/createNewProject";

        }
    }

    @GetMapping("/create-new-project")
    public ModelAndView creatNewProject() {
        ModelAndView m = new ModelAndView();
        m.addObject("CurrentUser", userService.getCurrentLoggedInUser());
        m.addObject("title", "Create New Project");
        m.addObject("project", new Project());
        m.setViewName("client/createNewProject");
        return m;
    }

    @GetMapping("/getSpDetails/{sp_id}")
    public ModelAndView getSpDetails(@PathVariable("sp_id") int sp_id) {
        User currUser = userService.getCurrentLoggedInUser();
        List<Project> projectList = projectService.getProjectsByClientId(currUser.getUserId());
        User spUser = userService.getUserByUserId(sp_id);
        ModelAndView m = new ModelAndView();
        m.addObject("CurrentUser", currUser);
        m.addObject("projectList", projectList);
        m.addObject("sp", spUser);
        m.setViewName("client/spDetails");
        return m;
    }

    @PostMapping("/askQuotation/{sp_id}")
    public ModelAndView askQuotation(@ModelAttribute("request") Requests requests, @PathVariable("sp_id") int sp_id) {
        if (requests.getProjectId() == null) {
            ModelAndView model = new ModelAndView();
            User currUser = userService.getCurrentLoggedInUser();
            model.addObject("errorMessageQuot", "Project name is required.");
            model.addObject("CurrentUser", currUser);
            List<Project> projectList = projectService.getProjectsByClientId(currUser.getUserId());
            User spUser = userService.getUserByUserId(sp_id);
            model.addObject("projectList", projectList);
            model.addObject("sp", spUser);
            model.setViewName("/client/spDetails");
            return model;
        }
        if (requests != null) {
            requests.setStatus(null);
            requests.setCreatedAt(LocalDateTime.now());
            Requests result = requestService.saveRequestToDb(requests);
            if (result.getRequestId() > 0) {
                ModelAndView m = setupClientDashboardModel();
                return m;
            }
            ModelAndView m = new ModelAndView();
            m.setViewName("/error");
            return m;
        }
        ModelAndView m = new ModelAndView();
        m.setViewName("/error");
        return m;

    }

    @GetMapping("/clientDocRequests")
    public ModelAndView getClientDocRequestsPage() {
        ModelAndView m = new ModelAndView();
        m.setViewName("/client/clientDocRequests");
        User CurrentUser = userService.getCurrentLoggedInUser();
        List<Requests> requestsList = requestService.getRequestsByClientWithoutRequestedDoc(CurrentUser);
        m.addObject("CurrentUser", CurrentUser);
        m.addObject("requestsList", requestsList);

        return m;
    }

    @GetMapping(value = "/downloadReqDoc/{requestId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> fetchDocument(@PathVariable int requestId) {
        byte[] document = requestService.findRequestedDocumentByRequestId(requestId);

        if (document != null) {

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\" RequestedDocument \"")
                    .body(document);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/AcceptQuotationRequestContract")
    public ModelAndView AcceptQuotationRequestContract(@RequestParam("QuotationRequestId") int QuotationRequestId) {
        Requests prevQuotReq = requestService.findByRequestId(QuotationRequestId);
        if (prevQuotReq != null) {
            prevQuotReq.setStatus(true);
            Requests prevReq = requestService.saveRequestToDb(prevQuotReq);
            if (prevReq.getRequestId() > 0) {
                Requests requests = new Requests();
                requests.setName("Contract");
                requests.setDocumentName("ContractDocument");
                requests.setStatus(null);
                requests.setCreatedAt(LocalDateTime.now());
                requests.setByClientId(prevReq.getByClientId());
                requests.setToSpId(prevReq.getToSpId());
                requests.setProjectId(prevReq.getProjectId());
                Requests result = requestService.saveRequestToDb(requests);
                if (result.getRequestId() > 0) {
                    ModelAndView m = setupClientDashboardModel();
                    return m;
                }
                ModelAndView m = new ModelAndView();
                m.setViewName("/error");
                return m;
            }

        }


        ModelAndView m = new ModelAndView();
        m.setViewName("/error");
        return m;

    }

    @PostMapping("/rejectDocRequest")
    public ModelAndView rejectDocRequest(@RequestParam("QuotationRequestId") int QuotationRequestId,
                                         @RequestParam("rejectionReason") String rejectionReason) {

        Requests prevQuotReq = requestService.findByRequestId(QuotationRequestId);

        if (prevQuotReq != null) {
         prevQuotReq.setRejectionReason(rejectionReason);
            prevQuotReq.setStatus(false);
            Requests prevReq = requestService.saveRequestToDb(prevQuotReq);
            if (prevReq.getRequestId() > 0) {

                ModelAndView m = setupClientDashboardModel();
                return m;
            }


        }
        ModelAndView m = new ModelAndView();
        m.setViewName("/error");
        return m;

    }


    @PostMapping("/updateClientProfile")
    public ModelAndView updateClientDetails(@ModelAttribute("CurrentUser") User user) {
        ModelAndView m = new ModelAndView();

        user.setIsApproved(true);
        User updatedUser = userService.saveNewUserToDb(user);
        List<User> userList = userService.getAllSp();
        m.addObject("title", "Service Providers");
        m.setViewName("/client/clientDashboard");
        m.addObject("userList", userList);
        m.addObject("CurrentUser", updatedUser);
        return m;
    }

    @PostMapping("/acceptContractAndRequestToSendDigitalContract")
    public ModelAndView acceptContractAndRequestToSendDigitalContract(@RequestParam("QuotationRequestId") int QuotationRequestId) {
        Requests prevReq1 = requestService.findByRequestId(QuotationRequestId);
        if (prevReq1 != null) {
            prevReq1.setStatus(true);
            Requests prevReq = requestService.saveRequestToDb(prevReq1);

            if (prevReq.getRequestId() > 0) {
                Requests requests = new Requests();
                requests.setName("SignContract");
                requests.setDocumentName("SignContract");
                requests.setStatus(null);
                requests.setCreatedAt(LocalDateTime.now());
                requests.setByClientId(prevReq.getByClientId());
                requests.setToSpId(prevReq.getToSpId());
                requests.setProjectId(prevReq.getProjectId());
                Requests result = requestService.saveRequestToDb(requests);
                if (result.getRequestId() > 0) {
                    ModelAndView m = setupClientDashboardModel();
                    return m;
                }
                ModelAndView m = new ModelAndView();
                m.setViewName("/error");
                return m;
            }
            ModelAndView m = new ModelAndView();
            m.setViewName("/error");
            return m;


        }


//        }


        ModelAndView m = new ModelAndView();
        m.setViewName("/error");
        return m;

    }


    @PostMapping("/UploadDigitalContract")
    public ModelAndView UploadDigitalContract(@RequestParam("contract") MultipartFile contract,
                                              @RequestParam("requestId") int requestId) throws IOException {
        Requests prevReq = requestService.findByRequestId(requestId);
        if (prevReq != null) {

            List<Requests> acceptedContractRequestList =
                    requestService.getRequestsWhereProjectsWithContractStatus(
                            prevReq.getProjectId(),
                            "SignContract", true);

            if (acceptedContractRequestList.isEmpty()) {
                prevReq.setRequestedDoc(contract.getBytes());
                prevReq.setFulfilledAt(LocalDateTime.now());
                Requests prevReqSaved = requestService.saveRequestToDb(prevReq);
                if (prevReqSaved.getRequestId() > 0) {


                    Requests requests = new Requests();
                    requests.setStatus(null);
                    requests.setName("ValidateContract");
                    requests.setDocumentName("NonValidatedContract");
                    requests.setByClientId(prevReqSaved.getByClientId());
                    requests.setToSpId(prevReqSaved.getToSpId());
                    requests.setProjectId(prevReqSaved.getProjectId());
                    requests.setCreatedAt(LocalDateTime.now());
                    requests.getProjectId().setSpOnProject(requests.getToSpId());
                    requests.setRequestedDoc(prevReqSaved.getRequestedDoc());
                    requestService.saveRequestToDb(requests);


                        ModelAndView m = setupClientDashboardModel();
                        return m;
                }

            } else {
                ModelAndView m = new ModelAndView();
                m.addObject("errorMessage", "You've Already Uploaded contract for Selected Project !!");
                m.setViewName("/error");
                return m;
            }
        }

            ModelAndView m = new ModelAndView();
        m.addObject("errorMessage", "Something went wrong ...try again later");
            m.setViewName("/error");
            return m;

    }


    private ProjectDocuments saveProjectDoc(MultipartFile file, ProjectDocuments pdoc) {

        try {
            pdoc.setAddedAt(LocalDateTime.now());
            pdoc.setDocumentData(file.getBytes());
            ProjectDocuments pDocSaved = pDocService.saveProjectDocTODb(pdoc);
            if (pDocSaved.getProjectDocumentId() > 0) {
                return pDocSaved;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ProjectDocuments();
    }





}
