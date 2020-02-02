package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.GetAddressBusinessService;
import com.upgrad.FoodOrderingApp.service.businness.GetStatesBusinessService;
import com.upgrad.FoodOrderingApp.service.businness.SaveAdressBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AddressController {

    @Autowired
    SaveAdressBusinessService saveAdressBusinessService;

    @RequestMapping(method = RequestMethod.POST, path = "/address", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(@RequestHeader("authorization") final String authorization,
                                                          final SaveAddressRequest saveAddressRequest) throws AddressNotFoundException, AuthorizationFailedException, SaveAddressException {

        String decode = authorization.split("Bearer ")[1];
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setUuid(UUID.randomUUID().toString());
        addressEntity.setFlat_buil_number(saveAddressRequest.getFlatBuildingName());
        addressEntity.setCity(saveAddressRequest.getCity());
        addressEntity.setLocality(saveAddressRequest.getLocality());
        addressEntity.setPincode(saveAddressRequest.getPincode());
        addressEntity.setActive(1);
        String state_uuid = saveAddressRequest.getStateUuid();

        AddressEntity savedAdress = saveAdressBusinessService.saveAdress(addressEntity, state_uuid, decode);

        SaveAddressResponse addressResponse = new SaveAddressResponse().id(savedAdress.getUuid()).status("ADDRESS SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SaveAddressResponse>(addressResponse, HttpStatus.CREATED);
    }

    @Autowired
    GetAddressBusinessService getAddressBusinessService;

    @RequestMapping(method = RequestMethod.GET, path = "/address/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> getAllAddress(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException{


        String decode = authorization.split("Bearer ")[1];
        List<AddressEntity> addressEntities = getAddressBusinessService.getAllAdress(decode);
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

    @Autowired
    GetStatesBusinessService getStatesBusinessService;

    @RequestMapping(method = RequestMethod.GET, path = "/states")
    public ResponseEntity<StatesListResponse> getAllStates(){

        List<StateEntity> stateEntities = getStatesBusinessService.getAllStates();
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

}
