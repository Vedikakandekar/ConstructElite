package com.constructElite.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int projectId;
    private String name;
    private LocalDateTime createdAt;
    private String projectDescription;

//    @JsonIgnore
//    @ManyToMany(mappedBy="projectByClient")
//    private List<User> projectByClient = new ArrayList<>();


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "projectId", referencedColumnName = "projectId")
    private List<ProjectDocuments> projectDocuments = new ArrayList<>();


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "projectId", referencedColumnName = "projectId")
    private List<ProjectImages> projectImages = new ArrayList<>();

}
