package com.constructElite.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@ToString
@NoArgsConstructor
@Entity
public class Milestones {


    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int milestoneId;

    @Getter
    private String milestoneName;

    @Getter
    private LocalDateTime completionDate;

    @Getter
    private String status;

    @Getter
    private String description ;


    @Getter
    @OneToOne(mappedBy = "milestoneId", cascade = CascadeType.ALL)
    private Payments paymentId;


    @ManyToOne(targetEntity = Project.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project projectId;


//    @JsonBackReference
    public Project getProjectId() {
        return projectId;
    }
}
