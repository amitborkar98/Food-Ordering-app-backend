package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogOutBusinessService {

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity logout(String token) throws AuthorizationFailedException {


        return null;
    }
}
