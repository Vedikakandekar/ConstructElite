package com.constructElite.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Setter
@ToString(exclude = {"spProjectList", "clientProjectList"})
@NoArgsConstructor
@Entity
public class Project {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int projectId;
    @Getter
    private String name;
    @Getter
    private String location;
    @Getter
    private String landSize;
    @Getter
    private LocalDateTime createdAt;
    @Getter
    private String budget;
    @Getter
    private String timeline;

    @Getter
    @Lob
    private String projectDescription;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "sp_id")
    private User spOnProject;


    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "client_id")
        private User clientOnProject;

    @JsonBackReference
    public User getSpOnProject() {
        return spOnProject;
    }

    @JsonBackReference
    public User getClientOnProject() {
        return clientOnProject;
    }
//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "projectId", referencedColumnName = "projectId")
//    private List<ProjectDocuments> projectDocuments = new ArrayList<>();
//
//
//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "projectId", referencedColumnName = "projectId")
//    private List<ProjectImages> projectImages = new ArrayList<>();

}
