package com.constructElite.repository;

import com.constructElite.Entity.Milestones;
import com.constructElite.Entity.Project;
import com.constructElite.Entity.Requests;
import com.constructElite.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MilestonesRepository extends JpaRepository<Milestones,Integer> {

    @Query("SELECT m FROM Milestones m WHERE m.projectId = ?1")
    List<Milestones> findMilestoneByProjectId(Project projectId);
}
