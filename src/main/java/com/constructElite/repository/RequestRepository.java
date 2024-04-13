package com.constructElite.repository;

import com.constructElite.Entity.Requests;
import com.constructElite.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Requests,Integer>{


    @Query("SELECT r FROM Requests r WHERE r.requestedDoc IS NULL  AND r.toSpId = ?1")
    List<Requests> findRequestsBySpIdAndEmptyDocument(User spId);

    @Query("SELECT r.requestId, r.name, r.documentName, r.status, r.createdAt, r.fulfilledAt, r.byClientId, r.toSpId, r.projectId FROM Requests r WHERE r.byClientId=?1")
    List<Object[]> findRequestsByClientIdExcludingRequestedDoc(User clientId);

    @Query("SELECT r.requestedDoc FROM Requests r WHERE r.requestId=?1")
    byte[] findRequestedDocumentByRequestId(int requestId);

    @Query("SELECT r FROM Requests r WHERE r.requestId=?1")
    Requests findByRequestId(int requestId);

}
