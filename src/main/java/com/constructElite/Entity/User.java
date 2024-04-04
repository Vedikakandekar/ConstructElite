package com.constructElite.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@ToString(exclude = {"spProjectList", "clientProjectList"})
@NoArgsConstructor
@Entity
public class User {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int  UserId;

    @Getter
    private String role;

    @Getter
    private String name;

    @Getter
    @Column(unique = true)
    private String phone;

    @Getter
    @Column(unique = true)
    private String email;

    @Getter
    @Column(length = 500)
    private String address;

    @Getter
    private String password;


    @Getter
    @Column(nullable = true)
    private Boolean isApproved;

//    @ManyToMany
//    @JoinTable(name = "client_project_sp",
//            joinColumns = @JoinColumn(name="user_id"),
//            inverseJoinColumns = @JoinColumn(name="project_id"))
//    private List<Project> projectByClient = new ArrayList<>();


    @OneToMany(targetEntity = Project.class,mappedBy ="spOnProject",orphanRemoval = true,cascade = CascadeType.ALL)
    private List<Project> spProjectList ;


    @OneToMany(targetEntity = Project.class,mappedBy ="clientOnProject",orphanRemoval = true,cascade = CascadeType.ALL)
    private List<Project> clientProjectList;

    @JsonManagedReference
    public int getUserId() {
        return UserId;
    }

    @JsonManagedReference
    public List<Project> getSpProjectList() {
        return spProjectList;
    }

    public List<Project> getClientProjectList() {
        return clientProjectList;
    }
}
