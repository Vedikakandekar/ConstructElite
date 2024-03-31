package com.constructElite.repository;

import com.constructElite.Entity.UserDocuments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDocumentRepository extends JpaRepository<UserDocuments,Integer> {


    Optional<UserDocuments> findBydocumentName(String name);
}
