package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class UpdateCustomerBusinessService {

    @Autowired
    CustomerDao customerDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomer(String authorization, CustomerEntity customerEntity) throws UpdateCustomerException, AuthorizationFailedException {

        CustomerAuthEntity customerAuthEntity = customerDao.getCustomerAuth(authorization);

        if(customerEntity.getFirstname() == null){
            throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
        }
        else if(customerAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001" ,"Customer is not Logged in.");
        }
        else if(customerAuthEntity.getLogout_at() != null){
            throw new AuthorizationFailedException("ATHR-002" ,"Customer is logged out. Log in again to access this endpoint.");
        }
        else if(customerAuthEntity.getExpires_at().isBefore(ZonedDateTime.now())){
            throw new AuthorizationFailedException("ATHR-003" ,"Your session is expired. Log in again to access this endpoint.");
        }
        else{
            CustomerEntity updateCustomer = customerAuthEntity.getCustomer();
            updateCustomer.setUuid(UUID.randomUUID().toString());
            updateCustomer.setFirstname(customerEntity.getFirstname());
            updateCustomer.setLastname(customerEntity.getLastname());
            return customerDao.updateCustomer(updateCustomer);
        }
    }
}

