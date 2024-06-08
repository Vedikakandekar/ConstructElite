package com.constructElite.repository;

import com.constructElite.Entity.ContractDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractDetailsRepository extends JpaRepository<ContractDetails,Integer> {

}
