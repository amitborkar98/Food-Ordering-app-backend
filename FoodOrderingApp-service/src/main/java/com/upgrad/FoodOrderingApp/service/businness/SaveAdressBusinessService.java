package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class SaveAdressBusinessService {

    @Autowired
    AddressDao addressDao;

    @Autowired
    CustomerDao customerDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity saveAdress(AddressEntity addressEntity, String state_uuid, String authorization) throws AddressNotFoundException, SaveAddressException, AuthorizationFailedException {


        CustomerAuthEntity customerAuthEntity = customerDao.getCustomerAuth(authorization);
        if(customerAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001" ,"Customer is not Logged in.");
        }
        else if(customerAuthEntity.getLogout_at() != null){
            throw new AuthorizationFailedException("ATHR-002" ,"Customer is logged out. Log in again to access this endpoint.");
        }
        else if(customerAuthEntity.getExpires_at().isBefore(ZonedDateTime.now())){
            throw new AuthorizationFailedException("ATHR-003" ,"Your session is expired. Log in again to access this endpoint.");
        }
        else if(addressEntity.getCity() == null || addressEntity.getFlat_buil_number() == null || addressEntity.getLocality() == null || addressEntity.getPincode() == null || state_uuid == null){
            throw new  SaveAddressException("SAR-001", "No field can be empty");
        }

        int number_count = 0;
        for (int i = 0; i < addressEntity.getPincode().length(); i++) {
            char ch = addressEntity.getPincode().charAt(i);
            ch = Character.toUpperCase(ch);
            //to check if the password contains a digit
            if (ch >= '0' && ch <= '9') number_count++;
        }

        if(number_count != 6 ){
            throw new SaveAddressException("SAR-002", "Invalid pincode");
        }

        StateEntity stateEntity = addressDao.getStateById(state_uuid);
        if(stateEntity == null){
            throw  new AddressNotFoundException("ANF-002", "No state by this id");
        }
        else{
            addressEntity.setState(stateEntity);
            CustomerAddressEntity customerAddressEntity = new CustomerAddressEntity();
            customerAddressEntity.setCustomer(customerAuthEntity.getCustomer());
            AddressEntity savedAdress = addressDao.saveAdress(addressEntity);
            customerAddressEntity.setAddress(savedAdress);
            addressDao.createCustomerAdress(customerAddressEntity);
            return savedAdress;
        }
    }

}
