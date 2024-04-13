package com.constructElite.repository;

import com.constructElite.Entity.User;
import com.constructElite.Entity.UserDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDocumentRepository extends JpaRepository<UserDocuments,Integer> {


    Optional<UserDocuments> findBydocumentName(String name);

    @Query("SELECT  u.documentId, u.documentName,u.addedAt, u.type FROM UserDocuments u where userId=?1")
    List<Object[]> findWithoutDocumentData(User user);

    @Query("SELECT ud FROM UserDocuments ud WHERE ud.userId = ?1 AND ud.documentName = ?2")
    UserDocuments findByClientAndDocName(User user,String name);


}
