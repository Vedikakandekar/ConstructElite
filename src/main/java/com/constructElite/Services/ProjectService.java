package com.constructElite.Services;

import com.constructElite.Entity.Project;
import com.constructElite.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepo;


    public Project getProjectByName(String name) {
        return projectRepo.findProjectByName(name);
    }

    public List<Project> getProjectsByClientId(int clientId)
    {
        return projectRepo.findByClientOnProjectUserId(clientId);
    }
    public List<Project> getProjectsBySpId(int spId)
    {
        return projectRepo.findBySpOnProjectUserId(spId);
    }

    public Project saveNewProjectToDb(Project p)
    {
        return projectRepo.save(p);
    }

    public Optional<Project> getProjectById(int id) {
        return projectRepo.findById(id);
    }
}
