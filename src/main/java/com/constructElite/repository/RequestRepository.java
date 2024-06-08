package com.constructElite.repository;

import com.constructElite.Entity.Project;
import com.constructElite.Entity.Requests;
import com.constructElite.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Requests,Integer>{


    @Query("SELECT r FROM Requests r WHERE r.toSpId = ?1 AND r.fulfilledAt IS NULL")
    List<Requests> findNonFulfilledRequestsBySpId(User spId);

    @Query("SELECT r.requestId, r.name, r.documentName, r.status, r.createdAt, r.fulfilledAt, r.byClientId, r.toSpId, r.projectId FROM Requests r WHERE r.byClientId=?1")
    List<Object[]> findRequestsByClientIdExcludingRequestedDoc(User clientId);

    @Query("SELECT r.requestedDoc FROM Requests r WHERE r.requestId=?1")
    byte[] findRequestedDocumentByRequestId(int requestId);

    @Query("SELECT r FROM Requests r WHERE r.requestId=?1")
    Requests findByRequestId(int requestId);

    @Query("SELECT r FROM Requests r WHERE r.projectId=?1 AND r.name=?2 AND r.status=?3 AND r.requestedDoc IS NOT NULL")
    List<Requests> findByProjectWithAcceptedContract(Project projectId, String name, Boolean status);

}
