package com.constructElite.Services;

import com.constructElite.Entity.Milestones;
import com.constructElite.Entity.Project;
import com.constructElite.Entity.Requests;
import com.constructElite.repository.MilestonesRepository;
import com.constructElite.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MilestonesService {

    @Autowired
    MilestonesRepository milestonesRepo;

    public List<Milestones> findByProject(Project project)
    {
        return milestonesRepo.findMilestoneByProjectId(project);
    }

    public Milestones saveNewMilestoneToDb(Milestones milestones)
    {
        return milestonesRepo.save(milestones);
    }
}
