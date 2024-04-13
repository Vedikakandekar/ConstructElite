package com.constructElite.controller;

import com.constructElite.Entity.Requests;
import com.constructElite.Entity.User;
import com.constructElite.Entity.UserDocuments;
import com.constructElite.Services.RequestService;
import com.constructElite.Services.UserDocumentsService;
import com.constructElite.Services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDateTime;
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


    private ModelAndView setupSPDashboardModel() {
        ModelAndView mav = new ModelAndView();
        User currUser =  userService.getCurrentLoggedInUser();
        mav.addObject("CurrentUser",currUser);
        mav.addObject("title", "Welcome "+currUser.getName());
        mav.setViewName("/sp/spDashboard");
        return mav;
    }


    @GetMapping("/spDashboard")
    public ModelAndView baseDash() {
        return setupSPDashboardModel();

    }

    @GetMapping("/spDocumentDetails")
    public ModelAndView spDocDetails() {
        ModelAndView m= new ModelAndView();
        User currUser = userService.getCurrentLoggedInUser();
        List<UserDocuments> userDocumentsList= userDocService.getUserDocumentWithoutContaint(currUser);
        UserDocuments license = userDocService.getUserDocByClientAndDocName(currUser,"license");
        if(license==null)
        {
           m.addObject("licenseDoc",true);
        }
        else
        {
            m.addObject("licenseDoc",false);
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

        if (document!=null) {

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getDocumentName() + "\"")
                    .body(document.getDocument());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/uploadLiscence")
    public ModelAndView saveDocumentToDb(@RequestParam("license") MultipartFile license)
    {
        saveUserDoc(license,"license",userService.getCurrentLoggedInUser());
        ModelAndView m= new ModelAndView();
        User currUser = userService.getCurrentLoggedInUser();
        List<UserDocuments> userDocumentsList= userDocService.getUserDocumentWithoutContaint(currUser);
        m.addObject("title", "Your Documents");
        m.addObject("userDocuments", userDocumentsList);
        m.addObject("CurrentUser", currUser);
        m.setViewName("/sp/spDocumentDetails");
        m.addObject("licenseDoc",false);
        return m;

    }

    private void saveUserDoc(MultipartFile file, String name, User currUser){
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
    public ModelAndView getDocumentRequestsPage()
    {
        ModelAndView m = new ModelAndView();
        User currUser = userService.getCurrentLoggedInUser();
        List<Requests> requestsList= requestService.getRequestBySpId(currUser);
        m.addObject("requestsList",requestsList);
//        System.out.println("        hello\n        hello\n     hello\n");
//        for (Requests request : requestsList) {
//            System.out.println(" Im in /docRequests Controller: " + request.getProjectId().getName());
//        }
        m.addObject("CurrentUser",currUser);
        m.setViewName("/sp/docRequests");
        return m;
    }

    @PostMapping("/uploadReqDocument")
    public ModelAndView saveRequestDocumentToDb(@ModelAttribute Requests request,
            @RequestParam("document") MultipartFile document)
    {
            saveReqDoc(document, request);
        ModelAndView m = new ModelAndView();
        User currUser = userService.getCurrentLoggedInUser();
        List<Requests> requestsList= requestService.getRequestBySpId(currUser);
        m.addObject("requestsList",requestsList);
        m.addObject("CurrentUser",currUser);
        m.setViewName("/sp/docRequests");
        return m;
    }


    private void saveReqDoc(MultipartFile file,Requests req)
    {
        try {
            req.setFulfilledAt(LocalDateTime.now());
//            Byte[] byteObjects = new Byte[file.getBytes().length];
//            for (int i = 0; i < file.getBytes().length; i++) {
//                byteObjects[i] = file.getBytes()[i];  // Autoboxing
//            }
//            req.setRequestedDoc(byteObjects);
            req.setRequestedDoc(file.getBytes());
            requestService.saveRequestToDb(req);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/updateSpProfile")
    public ModelAndView updateSPDetails(@ModelAttribute("CurrentUser") User user)
    {
        ModelAndView m = new ModelAndView();

        User updatedUser = userService.saveNewUserToDb(user);
        m.addObject("title", "Welcome "+updatedUser.getName());
        m.setViewName("/sp/spDashboard");
        m.addObject("CurrentUser",updatedUser);
        return m;
    }

}
