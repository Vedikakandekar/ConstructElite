package com.constructElite.Services;

import com.constructElite.Entity.User;
import com.constructElite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepo;

    public User findByEmail(String email)
    {
    return userRepo.findByEmail(email);
    }

    public User saveNewUserToDb(User user)
    {
        return userRepo.save(user);
    }

    public List<User> getNewSP()
    {
        return userRepo.findByIsApprovedNull();
    }

    public List<User> getApprovedSP(){ return userRepo.findByIsApproved();}

    public List<User> getDisapprovedSP(){ return userRepo.findByIsApprovedNot();}

    public List<User> findUsersByRoleClient(){ return userRepo.findUsersByRoleClient(); }


}
