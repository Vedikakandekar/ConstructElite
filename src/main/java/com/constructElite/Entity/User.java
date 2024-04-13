package com.constructElite.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@ToString
//        exclude = {"spProjectList", "clientProjectList", "reqListByClient", "reqListToSp","userDocumentsList"})
@NoArgsConstructor
@Entity
public class User {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int UserId;

    @Getter
    private String role;

    @Getter
    @NotBlank(message = "Name Shouldn't be Empty")
    private String name;

    @Getter
    @Column(unique = true)
    @Pattern(regexp="^[6-9]\\d{9}$", message="Invalid mobile number format")
    @NotBlank(message = "Phone Shouldn't be Empty")
    private String phone;

    @Getter
    @Column(unique = true)
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email Shouldn't be Empty")
    private String email;

    @Getter
    @Column(length = 500)
    @NotBlank(message = "Address Shouldn't be Empty")
    private String address;

    @Getter
    @NotBlank(message = "Password Shouldn't be Empty")
    private String password;


    @Getter
    @Column(nullable = true)
    private Boolean isApproved;



//
//    @OneToMany(targetEntity = Project.class, mappedBy = "spOnProject", orphanRemoval = true, cascade = CascadeType.ALL)
//    private List<Project> spProjectList;
//
//
//    @OneToMany(targetEntity = Project.class, mappedBy = "clientOnProject", orphanRemoval = true, cascade = CascadeType.ALL)
//    private List<Project> clientProjectList;
//
//    @OneToMany(targetEntity = Requests.class, mappedBy = "byClientId", orphanRemoval = true, cascade = CascadeType.ALL)
//    private List<Requests> reqListByClient;
//
//    @OneToMany(targetEntity = Requests.class, mappedBy = "toSpId", orphanRemoval = true, cascade = CascadeType.ALL)
//    private List<Requests> reqListToSp;
//
//
//    @OneToMany(targetEntity = UserDocuments.class, mappedBy = "userId", orphanRemoval = true, cascade = CascadeType.ALL)
//    private List<UserDocuments> userDocumentsList;
//
//    @JsonManagedReference
//    public List<UserDocuments> getUserDocumentsList() {
//        return userDocumentsList;
//    }
//
//
//    @JsonManagedReference
//    public List<Requests> getReqListByUser() {
//        return reqListByClient;
//    }
//
//    @JsonManagedReference
//    public List<Requests> getReqListToUser() {
//        return reqListToSp;
//    }
//
//
//    @JsonManagedReference
//    public List<Project> getSpProjectList() {
//        return spProjectList;
//    }
//
//    @JsonManagedReference
//    public List<Project> getClientProjectList() {
//        return clientProjectList;
//    }


}
