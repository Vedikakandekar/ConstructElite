package com.constructElite.Entity;


import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int  UserId;

    private String role;

    private String name;

    @Column(unique = true)
    private String phone;

    @Column(unique = true)
    private String email;

    @Column(length = 500)
    private String address;

    private String password;


    @Column(nullable = true)
    private Boolean isApproved;

//    @ManyToMany
//    @JoinTable(name = "client_project_sp",
//            joinColumns = @JoinColumn(name="user_id"),
//            inverseJoinColumns = @JoinColumn(name="project_id"))
//    private List<Project> projectByClient = new ArrayList<>();


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "sp_user_id", referencedColumnName = "UserId")
    private List<Project> spOnProject = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_user_id", referencedColumnName = "UserId")
    private List<Project> clientOnProject = new ArrayList<>();







}
