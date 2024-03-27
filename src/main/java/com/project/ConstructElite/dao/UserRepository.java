package com.project.ConstructElite.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ConstructElite.Entities.User;

@Repository

public interface UserRepository extends JpaRepository<User, Integer> {

	
	User findByUserEmail(String email);
	
}
