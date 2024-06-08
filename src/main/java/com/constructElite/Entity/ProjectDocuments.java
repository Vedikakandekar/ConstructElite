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
public class ProjectDocuments {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int projectDocumentId;

    @Getter
    private String documentName;

    @Getter
    private String documentDescription;

    @Lob
    @Getter
    @Column(columnDefinition = "LONGBLOB")
    private byte[] documentData;

    @Getter
    private LocalDateTime addedAt;

    @ManyToOne(targetEntity = Project.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project projectId;


   @JsonBackReference
    public Project getProjectId() {
        return projectId;
    }



}
