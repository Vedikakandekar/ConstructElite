package com.constructElite.repository;

import com.constructElite.Entity.Project;
import com.constructElite.Entity.ProjectDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectDocumentsRepository extends JpaRepository<ProjectDocuments,Integer> {


}
