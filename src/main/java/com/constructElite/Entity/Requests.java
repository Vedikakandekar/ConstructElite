package com.constructElite.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@NoArgsConstructor
@ToString
@Entity
public class Requests {


    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int  requestId;

    @Getter
    private String name;

    @Getter
    private String documentName;

    @Getter
    private String rejectionReason;

    @Getter
    private Boolean status;

    @Getter
    private LocalDateTime createdAt;

    @Getter
    private LocalDateTime fulfilledAt;

    @Getter
    @Lob
    @Column(columnDefinition = "LONGBLOB", nullable = true)
    private byte[] requestedDoc;


//    @Getter
//    @OneToOne(mappedBy = "requestId", cascade = CascadeType.ALL)
//    private ContractDetails contractId;


    @ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "by_client_id")
    private User byClientId;


    @ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "to_sp_id")
    private User toSpId;

    @ManyToOne(targetEntity = Project.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project projectId;

//    @JsonBackReference
    public Project getProjectId() {
        return projectId;
    }

//    @JsonBackReference
    public User getToSpId() {
        return toSpId;
    }

//    @JsonBackReference
    public User getByClientId() {
        return byClientId;
    }

}
