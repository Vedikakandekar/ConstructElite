package com.constructElite.Services;

import com.constructElite.Entity.Project;
import com.constructElite.Entity.Requests;
import com.constructElite.Entity.User;
import com.constructElite.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    @Autowired
    RequestRepository requestRepo;

    public Requests saveRequestToDb(Requests requests)
    {
        return requestRepo.save(requests);
    }

    public List<Requests> getRequestBySpId(User sp)
    {
        return requestRepo.findRequestsBySpIdAndEmptyDocument(sp);
    }

    public Requests findByRequestId(int requestId)
    {
        return requestRepo.findByRequestId(requestId);
    }

    private List<Requests> mapToRequests(List<Object[]> resultList) {
        List<Requests> requestsList = new ArrayList<>();
        for (Object[] row : resultList) {
            Requests request = new Requests();
            request.setRequestId((Integer) row[0]);
            request.setName((String) row[1]);
            request.setDocumentName((String) row[2]);
            request.setStatus((Boolean) row[3]);
            request.setCreatedAt((LocalDateTime) row[4]);
            request.setFulfilledAt((LocalDateTime) row[5]);
            request.setByClientId((User) row[6]);
            request.setToSpId((User) row[7]);
            request.setProjectId((Project) row[8]);
            requestsList.add(request);
        }
        return requestsList;
    }

    public List<Requests> getRequestsByClientWithoutRequestedDoc(User client)
    {
        List<Object[]> objList = requestRepo.findRequestsByClientIdExcludingRequestedDoc(client);
        return mapToRequests(objList);
    }

    public byte[] findRequestedDocumentByRequestId(int requestId)
    {
       return requestRepo.findRequestedDocumentByRequestId(requestId);
    }
}
