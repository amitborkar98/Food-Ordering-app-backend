package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogOutBusinessService {

    @Autowired
    CustomerDao customerDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity logout(String authorization) throws AuthorizationFailedException {

        CustomerAuthEntity customerAuthEntity = customerDao.getCustomerAuth(authorization);
        if(customerAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001" ,"Customer is not Logged in.");
        }


        return customerAuthEntity.getCustomer();
    }
}
