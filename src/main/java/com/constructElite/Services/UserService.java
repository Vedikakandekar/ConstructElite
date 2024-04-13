package com.constructElite.Services;

import com.constructElite.Entity.User;
import com.constructElite.config.CustomUserDetails;
import com.constructElite.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepo;


    public User getUserByUserId(int id)
    {
        return userRepo.findByUserId(id);
    }
    public Optional<User> getUserById(int id) {
        return userRepo.findById(id);
    }

    public List<User> searchAllUsers(String searchTerm)
    {
        return userRepo.searchAllUsers(searchTerm);
    }

    public List<User> searchSPUsers(String searchTerm) {
        return userRepo.searchSPUsers(searchTerm);
    }

    public List<User> getAllSp()
    {
        return userRepo.findByIsApproved();
    }
    public User getCurrentLoggedInUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return findByEmail(email);
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User saveNewUserToDb(User user) {
        return userRepo.save(user);
    }

    public List<User> getNewSP() {
        return userRepo.findByIsApprovedNull();
    }

    public List<User> getApprovedSP() {
        return userRepo.findByIsApproved();
    }

    public List<User> getDisapprovedSP() {
        return userRepo.findByIsApprovedNot();
    }

    public List<User> findUsersByRoleClient() {
        return userRepo.findUsersByRoleClient();
    }


}
