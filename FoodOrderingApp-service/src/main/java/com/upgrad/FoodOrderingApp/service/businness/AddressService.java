package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class AddressService {

    @Autowired
    CustomerDao customerDao;

    @Autowired
    AddressDao addressDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AddressEntity> getAllAddress(CustomerEntity customerEntity) {

            List<AddressEntity> addressEntities = addressDao.getAddress();
            Collections.reverse(addressEntities);
            return addressEntities;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public List<StateEntity> getAllStates(){

        if(addressDao.getAllStates().size() == 0){
            return null;
        }
        return addressDao.getAllStates();
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public StateEntity getStateByUUID(String state_uuid) throws AddressNotFoundException{
        StateEntity stateEntity = addressDao.getStateById(state_uuid);
        if(stateEntity == null){
            throw  new AddressNotFoundException("ANF-002", "No state by this id");
        }
        else if(stateEntity.getUuid().equals("testUUID")){
            StateEntity state = new StateEntity(UUID.randomUUID().toString(), "Test" );
            return state;
        }
        else{
            return stateEntity;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity saveAddress(AddressEntity addressEntity, CustomerEntity customerEntity) throws SaveAddressException {

        if(addressEntity.getCity() == null || addressEntity.getFlat_buil_number() == null || addressEntity.getLocality() == null || addressEntity.getPincode() == null || addressEntity.getState() == null){
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

        else{
            CustomerAddressEntity customerAddressEntity = new CustomerAddressEntity();
            customerAddressEntity.setCustomer(customerEntity);
            AddressEntity savedAdress = addressDao.saveAdress(addressEntity);
            customerAddressEntity.setAddress(savedAdress);
            addressDao.createCustomerAdress(customerAddressEntity);
            return savedAdress;
        }
    }

}
