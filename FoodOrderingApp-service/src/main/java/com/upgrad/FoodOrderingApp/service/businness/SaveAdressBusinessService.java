package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaveAdressBusinessService {

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity saveAdress(AddressEntity addressEntity, String state_uuid) throws SaveAddressException, AuthorizationFailedException {


        return null;
    }

}
