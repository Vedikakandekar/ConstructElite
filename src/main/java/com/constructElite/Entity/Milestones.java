package com.constructElite.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "milestoneName Should NOT  be Empty !!")
    private String milestoneName;

    @Getter
    @NotBlank(message = "completionDate Should NOT  be Empty !!")
    private String completionDate;

    @Getter
    @NotBlank(message = "status Should NOT  be Empty !!")
    private String status;

    @Getter
    @NotBlank(message = "description Should NOT  be Empty !!")
    private String description;

    @Getter
    @NotBlank(message = " Amount  Should NOT  be Empty !!")
    private String amountToBePaid;


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
