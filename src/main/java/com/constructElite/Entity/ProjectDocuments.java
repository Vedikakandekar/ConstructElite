package com.constructElite.Entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class ProjectDocuments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int projectDocumentId;

    private String documentName;

    private String documentDescription;

    private LocalDateTime addedAt;



}
