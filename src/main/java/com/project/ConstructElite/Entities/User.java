package com.project.ConstructElite.Entities;


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
    private int userId;
    private String userRole;
    private String UserName;
    @Column(unique = true)
    private String UserPhone;
    @Column(unique = true)
    private String UserEmail;

    @Column(length = 500)
    private String UserAddress;
    @Column(unique = true)
    private String UserAdhar;
    @Column(unique = true)
    private String UserPan;
    private String UserPassword;

    @ManyToMany
    @JoinTable(name = "user_project",
               joinColumns = @JoinColumn(name="user_id"),
               inverseJoinColumns = @JoinColumn(name="project_id"))
    private List<Project> userProject = new ArrayList<>();







}
