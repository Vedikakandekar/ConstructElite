package com.constructElite.Entity;

import jakarta.persistence.*;
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
public class UserDocuments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int documentId;

    @Lob
    private byte[] document;
    private String documentName;

    private String type;

    private LocalDateTime addedAt;
}
