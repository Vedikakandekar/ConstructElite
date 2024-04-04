package com.constructElite.Services;

import com.constructElite.Entity.Project;
import com.constructElite.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Project saveNewProjectToDb(Project p)
    {
        return projectRepo.save(p);
    }
}
