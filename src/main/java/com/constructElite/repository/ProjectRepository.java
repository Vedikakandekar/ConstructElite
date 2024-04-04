package com.constructElite.repository;

import com.constructElite.Entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProjectRepository extends JpaRepository<Project,Integer> {


    @Query("SELECT p FROM Project p WHERE p.name=?1")
    Project findProjectByName(String name);


//    @Query("SELECT p FROM Project p WHERE p.clientOnProject.UserId=?1")
//    List<Project> getProjectByClientId(int clientId);

    @Query("SELECT p FROM Project p WHERE p.clientOnProject.UserId =?1")
    List<Project> findByClientOnProjectUserId(int clientId);

}
