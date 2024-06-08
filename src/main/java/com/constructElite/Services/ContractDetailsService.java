package com.constructElite.Services;

import com.constructElite.Entity.ContractDetails;
import com.constructElite.repository.ContractDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractDetailsService {

    @Autowired
    ContractDetailsRepository contractRepo;

    public ContractDetails saveContractToDb(ContractDetails contract)
    {
        return contractRepo.save(contract);
    }

}
