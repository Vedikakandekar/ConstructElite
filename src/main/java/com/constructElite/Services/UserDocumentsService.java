package com.constructElite.Services;

import com.constructElite.Entity.User;
import com.constructElite.Entity.UserDocuments;
import com.constructElite.repository.UserDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserDocumentsService {

    @Autowired
    UserDocumentRepository userDocRepo;

    public UserDocuments saveUserDocToDb(UserDocuments userDoc)
    {
        return userDocRepo.save(userDoc);
    }

    public List<UserDocuments> getUserDocumentWithoutContaint(User user)
    {
        List<Object[]> resultList = userDocRepo.findWithoutDocumentData(user);
        return mapToUserDocuments(resultList);
    }

    private List<UserDocuments> mapToUserDocuments(List<Object[]> resultList) {
        List<UserDocuments> userDocumentsList = new ArrayList<>();
        for (Object[] result : resultList) {

            UserDocuments userDocuments = new UserDocuments();
            userDocuments.setDocumentId((Integer) result[0]);
         userDocuments.setDocumentName((String) result[1]);
            userDocuments.setAddedAt((LocalDateTime) result[2]);
            userDocuments.setType((String) result[3]);
            userDocumentsList.add(userDocuments);
        }


        return userDocumentsList;
    }


    public UserDocuments getUserDocumentById(int id) {
        return userDocRepo.findById(id).orElse(null);
    }

    public UserDocuments getUserDocByClientAndDocName(User user,String name)
    {
        return userDocRepo.findByClientAndDocName(user,name);
    }

}



