package com.constructElite.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.List;


@Setter
@ToString(exclude = {"requestsList","projectDocumentList","milestonesList"})
@NoArgsConstructor
@Entity
public class Project {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int projectId;

    @Getter
    @NotBlank(message = "Name Should NOT  be Empty and Must be UNIQUE")
    private String name;

    @Getter
    @NotBlank(message = "Location Should NOT be Empty")
    private String location;

    @Getter
    @NotBlank(message = "LandSize Should NOT be Empty")
    private String landSize;

    @Getter
    private LocalDateTime createdAt;

    @Getter
    @NotBlank(message = "Budget Should NOT be Empty")
    private String budget;

    @Getter
    @NotBlank(message = "Enter Estimated timeline, it Should NOT be Empty")
    private String timeline;

    @Getter
    @Lob
    @NotBlank(message = "This is detailed Document for your Project, Fill up every information regarding your project")
    private String projectDescription;


//    @Getter
//    @OneToOne(mappedBy = "projectId", cascade = CascadeType.ALL)
//    private ContractDetails contractId;


//    @OneToMany(targetEntity = Requests.class, mappedBy = "projectId", orphanRemoval = true, cascade = CascadeType.ALL)
//    private List<Requests> requestsList;
//
//
//
//    @OneToMany(targetEntity = ProjectDocuments.class, mappedBy = "projectId", orphanRemoval = true, cascade = CascadeType.ALL)
//    private List<ProjectDocuments> projectDocumentList;
//
//
//    @OneToMany(targetEntity = Milestones.class, mappedBy = "milestoneId", orphanRemoval = true, cascade = CascadeType.ALL)
//    private List<Milestones> milestonesList;
//


    @ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "sp_id")
    private User spOnProject;


    @ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private User clientOnProject;

//    @JsonManagedReference
//    public List<ProjectDocuments> getProjectDocumentsList() {
//        return projectDocumentList;
//    }
//
//    @JsonManagedReference
//    public List<Requests> getRequestsList() {
//        return requestsList;
//    }
//
//    @JsonManagedReference
//    public List<Milestones> getMilstonesList() {
//        return milestonesList;
//    }
//

//    @JsonBackReference
    public User getSpOnProject() {
        return spOnProject;
    }

//    @JsonBackReference
    public User getClientOnProject() {
        return clientOnProject;
    }


}
