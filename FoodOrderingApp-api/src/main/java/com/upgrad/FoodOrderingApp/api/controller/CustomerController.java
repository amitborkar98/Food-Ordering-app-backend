package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.LogInBusinessService;
import com.upgrad.FoodOrderingApp.service.businness.LogOutBusinessService;
import com.upgrad.FoodOrderingApp.service.businness.SignUpBusinessService;
import com.upgrad.FoodOrderingApp.service.businness.UpdateCustomerBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
@CrossOrigin
public class CustomerController {

    @Autowired
    SignUpBusinessService signUpBusinessService;

    @RequestMapping(method = RequestMethod.POST, path = "/customer/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupCustomerResponse> signup(final SignupCustomerRequest signupCustomerRequest) throws SignUpRestrictedException {

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setUuid(UUID.randomUUID().toString());
        customerEntity.setFirstname(signupCustomerRequest.getFirstName());
        customerEntity.setLastname(signupCustomerRequest.getLastName());
        customerEntity.setContact_number(signupCustomerRequest.getContactNumber());
        customerEntity.setEmail(signupCustomerRequest.getEmailAddress());
        customerEntity.setPassword(signupCustomerRequest.getPassword());
        customerEntity.setSalt("1234abc");

        final CustomerEntity registeredCustomer = signUpBusinessService.signupCustomer(customerEntity);

        SignupCustomerResponse signupCustomerResponse = new SignupCustomerResponse().id(registeredCustomer.getUuid()).status("CUSTOMER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupCustomerResponse>(signupCustomerResponse, HttpStatus.CREATED);
    }

    @Autowired
    LogInBusinessService logInBusinessService;

    @RequestMapping(method = RequestMethod.POST, path = "/customer/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
     public ResponseEntity<LoginResponse> login(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {

        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodedText = new String(decode);
        String[] decodedArray = decodedText.split(":");


        CustomerAuthEntity customerAuthEntity = logInBusinessService.logIn(decodedArray[0],decodedArray[1]);
        CustomerEntity customerEntity = customerAuthEntity.getCustomer();

        LoginResponse loginResponse = new LoginResponse().id(customerEntity.getUuid()).message("LOGGED IN SUCCESSFULLY").firstName(customerEntity.getFirstname()).lastName(customerEntity.getLastname()).emailAddress(customerEntity.getEmail()).contactNumber(customerEntity.getContact_number());

        List<String> header = new ArrayList<>();
        header.add("access-token");

        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", customerAuthEntity.getAccess_token());
        headers.setAccessControlExposeHeaders(header);

        return new ResponseEntity<LoginResponse>(loginResponse, headers, HttpStatus.OK);
     }

     @Autowired
     LogOutBusinessService logOutBusinessService;

     @RequestMapping(method = RequestMethod.POST, path = "/customer/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
     public ResponseEntity<LogoutResponse> logout(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {

        CustomerEntity customerEntity = logOutBusinessService.logout(authorization);

        LogoutResponse logoutResponse = new LogoutResponse().id(customerEntity.getUuid()).message("LOGGED OUT SUCCESSFULLY");
        return new ResponseEntity<LogoutResponse>(logoutResponse, HttpStatus.OK);
     }

     @Autowired
     UpdateCustomerBusinessService updateCustomerBusinessService;

     @RequestMapping(method = RequestMethod.PUT, path = "/customer", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
     public ResponseEntity<UpdateCustomerResponse> updateCustomer(@RequestHeader("authorization") final String authorization,
                                                                  final UpdateCustomerRequest updateCustomerRequest) throws AuthorizationFailedException, UpdateCustomerException {

         String decode = authorization.split("Bearer ")[1];

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setFirstname(updateCustomerRequest.getFirstName());
        customerEntity.setLastname(updateCustomerRequest.getLastName());

        CustomerEntity updatedCustomer =updateCustomerBusinessService.updateCustomer(decode, customerEntity);

        UpdateCustomerResponse updateCustomerResponse = new UpdateCustomerResponse().id(updatedCustomer.getUuid()).firstName(updatedCustomer.getFirstname()).lastName(updatedCustomer.getLastname()).status("CUSTOMER DETAILS UPDATED SUCCESSFULLY");
        return new ResponseEntity<UpdateCustomerResponse>(updateCustomerResponse, HttpStatus.OK);
     }

}

