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
public class UserDocuments {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int documentId;

    @Lob
    @Getter
    @Column(columnDefinition = "LONGBLOB")
    private byte[] document;

    @Getter
    private String documentName;

    @Getter
    private String type;

    @Getter
    private LocalDateTime addedAt;


    @ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_Id")
    private User userId;

//    @JsonBackReference
    public User getUserId() {
        return userId;
    }
}
