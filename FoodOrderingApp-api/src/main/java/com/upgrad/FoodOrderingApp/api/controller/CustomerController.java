package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.*;
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
    CustomerService customerService;

    @RequestMapping(method = RequestMethod.POST, path = "/customer/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupCustomerResponse> signup(@RequestBody(required = false) final SignupCustomerRequest signupCustomerRequest) throws SignUpRestrictedException {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setUuid(UUID.randomUUID().toString());
        customerEntity.setFirstName(signupCustomerRequest.getFirstName());
        customerEntity.setLastName(signupCustomerRequest.getLastName());
        customerEntity.setContact_number(signupCustomerRequest.getContactNumber());
        customerEntity.setEmail(signupCustomerRequest.getEmailAddress());
        customerEntity.setPassword(signupCustomerRequest.getPassword());
        customerEntity.setSalt("1234abc");
        final CustomerEntity registeredCustomer = customerService.saveCustomer(customerEntity);
        SignupCustomerResponse signupCustomerResponse = new SignupCustomerResponse().id(registeredCustomer.getUuid()).status("CUSTOMER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupCustomerResponse>(signupCustomerResponse, HttpStatus.CREATED);
    }


    @RequestMapping(method = RequestMethod.POST, path = "/customer/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
     public ResponseEntity<LoginResponse> authenticate(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {
        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodedText = new String(decode);
        String[] decodedArray = decodedText.split(":");
        if(decodedArray.length == 0){
            String [] s =new String[2];
            s[0] = "Invalid";
            s[1] = "Invalid";
            customerService.authenticate(s[0],s[1]);
        }
        else {
            CustomerAuthEntity customerAuthEntity = customerService.authenticate(decodedArray[0], decodedArray[1]);
            CustomerEntity customerEntity = customerAuthEntity.getCustomer();
            LoginResponse loginResponse = new LoginResponse().id(customerEntity.getUuid()).message("LOGGED IN SUCCESSFULLY").firstName(customerEntity.getFirstname()).lastName(customerEntity.getLastame()).emailAddress(customerEntity.getEmail()).contactNumber(customerEntity.getContact_number());
            List<String> header = new ArrayList<>();
            header.add("access-token");
            HttpHeaders headers = new HttpHeaders();
            headers.add("access-token", customerAuthEntity.getAccess_token());
            headers.setAccessControlExposeHeaders(header);
            return new ResponseEntity<LoginResponse>(loginResponse, headers, HttpStatus.OK);
        }
        return null;
    }


     @RequestMapping(method = RequestMethod.POST, path = "/customer/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
     public ResponseEntity<LogoutResponse> logout(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
         String decode = authorization.split("Bearer ")[1];
         CustomerAuthEntity customerAuthEntity= customerService.logout(decode);
         LogoutResponse logoutResponse = new LogoutResponse().id(customerAuthEntity.getCustomer().getUuid()).message("LOGGED OUT SUCCESSFULLY");
        return new ResponseEntity<LogoutResponse>(logoutResponse, HttpStatus.OK);
     }


     @RequestMapping(method = RequestMethod.PUT, path = "/customer", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
     public ResponseEntity<UpdateCustomerResponse> updateCustomer(@RequestHeader("authorization") final String authorization,
                                                                  @RequestBody(required = false) final UpdateCustomerRequest updateCustomerRequest) throws UpdateCustomerException, AuthorizationFailedException {

        String decode = authorization.split("Bearer ")[1];
        //not working in service class, hence instantiated here
        if(updateCustomerRequest.getFirstName() == null){
            throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
        }
         CustomerEntity customerEntity = customerService.getCustomer(decode);
         customerEntity.setFirstName(updateCustomerRequest.getFirstName());
         customerEntity.setLastName(updateCustomerRequest.getLastName());
         CustomerEntity updatedCustomer = customerService.updateCustomer(customerEntity);
         UpdateCustomerResponse updateCustomerResponse = new UpdateCustomerResponse().id(updatedCustomer.getUuid()).firstName(updatedCustomer.getFirstname()).lastName(updatedCustomer.getLastame()).status("CUSTOMER DETAILS UPDATED SUCCESSFULLY");
         return new ResponseEntity<UpdateCustomerResponse>(updateCustomerResponse, HttpStatus.OK);
     }


    @RequestMapping(method = RequestMethod.PUT, path = "/customer/password", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
     public ResponseEntity<UpdatePasswordResponse> updatePassword(@RequestHeader("authorization") final String authorization,
                                                                 @RequestBody(required = false) final UpdatePasswordRequest updatePasswordRequest) throws UpdateCustomerException, AuthorizationFailedException{

        String decode = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(decode);
        CustomerEntity updateCustomerPassword = customerService.updateCustomerPassword(updatePasswordRequest.getOldPassword(), updatePasswordRequest.getNewPassword(), customerEntity);
        UpdatePasswordResponse updatePasswordResponse = new UpdatePasswordResponse().id(updateCustomerPassword.getUuid()).status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");
        return new ResponseEntity<UpdatePasswordResponse>(updatePasswordResponse, HttpStatus.OK);
     }
}



