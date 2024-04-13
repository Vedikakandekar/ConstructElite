package com.constructElite.repository;


import com.constructElite.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface UserRepository extends JpaRepository<User, Integer> {


    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.UserId=?1")
    User findByUserId(int id);

    @Query("SELECT u FROM User u WHERE (u.name LIKE %:searchTerm% OR u.address LIKE %:searchTerm% OR u.email LIKE %:searchTerm%) AND u.role = 'ROLE_CONTRACTOR'")
    List<User> searchSPUsers(@Param("searchTerm") String searchTerm);

    @Query("SELECT u FROM User u WHERE u.role='ROLE_CONTRACTOR'")
    List<User> findAllSp();

    @Query("SELECT u FROM User u WHERE u.name LIKE %:searchTerm% OR u.address LIKE %:searchTerm% OR u.email LIKE %:searchTerm%")
    List<User> searchAllUsers(@Param("searchTerm") String searchTerm);
    @Query("SELECT u FROM User u WHERE u.isApproved IS NULL")
    List<User> findByIsApprovedNull();

    @Query("SELECT u FROM User u WHERE u.isApproved=true and u.role='ROLE_CONTRACTOR'")
    List<User> findByIsApproved();

    @Query("SELECT u FROM User u WHERE u.isApproved=false")
    List<User> findByIsApprovedNot();

    @Query("SELECT u FROM User u WHERE u.role='ROLE_CLIENT'")
    List<User> findUsersByRoleClient();



}
