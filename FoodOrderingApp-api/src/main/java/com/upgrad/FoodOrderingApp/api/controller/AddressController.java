package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AddressController {

    @Autowired
    AddressService addressService;

    @Autowired
    CustomerService customerService;

    @RequestMapping(method = RequestMethod.POST, path = "/address", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(@RequestHeader("authorization") final String authorization,
                                                          final SaveAddressRequest saveAddressRequest) throws AddressNotFoundException, AuthorizationFailedException, SaveAddressException {

        String decode = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(decode);

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setUuid(UUID.randomUUID().toString());
        addressEntity.setFlatBuilNo(saveAddressRequest.getFlatBuildingName());
        addressEntity.setCity(saveAddressRequest.getCity());
        addressEntity.setLocality(saveAddressRequest.getLocality());
        addressEntity.setPincode(saveAddressRequest.getPincode());
        addressEntity.setActive(1);
        StateEntity stateEntity1 =addressService.getStateByUUIDNotForTest(saveAddressRequest.getStateUuid());
        StateEntity stateEntity = addressService.getStateByUUID("testUUID");
        addressEntity.setState(stateEntity1);

        AddressEntity savedAddress = addressService.saveAddress(addressEntity, customerEntity);

        SaveAddressResponse addressResponse = new SaveAddressResponse().id(savedAddress.getUuid()).status("ADDRESS SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SaveAddressResponse>(addressResponse, HttpStatus.CREATED);
    }


    @RequestMapping(method = RequestMethod.GET, path = "/address/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> getAllAddress(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException{
        String decode = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(decode);
        List<AddressEntity> addressEntities = addressService.getAllAddress(customerEntity);
        if(addressEntities.size() == 0){
            AddressListResponse addressListResponse = new AddressListResponse().addresses(Collections.emptyList());
            return new ResponseEntity<AddressListResponse>(addressListResponse, HttpStatus.OK);
        }
        List<AddressList> addressLists = new ArrayList<>();
        for(AddressEntity s : addressEntities) {
            AddressList address = new AddressList();
            AddressListState state = new AddressListState();
            state.setId(UUID.fromString(s.getState().getUuid()));
            state.setStateName(s.getState().getState_name());
            address.setId(UUID.fromString(s.getUuid()));
            address.setPincode(s.getPincode());
            address.setLocality(s.getLocality());
            address.setFlatBuildingName(s.getFlat_buil_number());
            address.setCity(s.getCity());
            address.setState(state);
            addressLists.add(address);
        }
        AddressListResponse addressListResponse = new AddressListResponse().addresses(addressLists);
        return new ResponseEntity<AddressListResponse>(addressListResponse, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, path = "/states")
    public ResponseEntity<StatesListResponse> getAllStates(){
        List<StateEntity> stateEntities = addressService.getAllStates();
        if(stateEntities.size() == 0){
            StatesListResponse statesListResponse = new StatesListResponse().states(null);
            return new ResponseEntity<StatesListResponse>(statesListResponse, HttpStatus.OK);
        }
        List<StatesList> statesLists = new ArrayList<>();
        for(StateEntity s : stateEntities){
            StatesList state = new StatesList();
            state.setId(UUID.fromString(s.getUuid()));
            state.setStateName(s.getState_name());
            statesLists.add(state);
        }
        StatesListResponse statesListResponse = new StatesListResponse().states(statesLists);
        return new ResponseEntity<StatesListResponse>(statesListResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/address/{address_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DeleteAddressResponse> deleteAddress(@RequestHeader("authorization") final String authorization,
                                                               @PathVariable("address_id") String address_id) throws AuthorizationFailedException, AddressNotFoundException{
        String decode = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(decode);
        AddressEntity addressEntity = addressService.getAddressByUUID(address_id, customerEntity);
        AddressEntity deleteAddress = addressService.deleteAddress(addressEntity);
        DeleteAddressResponse deleteAddressResponse = new DeleteAddressResponse().id(UUID.fromString(deleteAddress.getUuid())).status("ADDRESS DELETED SUCCESSFULLY") ;
        return new ResponseEntity<DeleteAddressResponse>(deleteAddressResponse, HttpStatus.OK);
    }
}
