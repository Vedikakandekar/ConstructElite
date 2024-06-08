package com.constructElite.Services;

import com.constructElite.Entity.ProjectDocuments;
import com.constructElite.repository.ProjectDocumentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectDocumentsService {

    @Autowired
    ProjectDocumentsRepository pDocRepo;

    public ProjectDocuments saveProjectDocTODb(ProjectDocuments pDoc)
    {
        return pDocRepo.save(pDoc);
    }

    public Optional<ProjectDocuments>  findProjectDocument(int projectDocumentId)
    {
        return  pDocRepo.findById(projectDocumentId);
    }


}
