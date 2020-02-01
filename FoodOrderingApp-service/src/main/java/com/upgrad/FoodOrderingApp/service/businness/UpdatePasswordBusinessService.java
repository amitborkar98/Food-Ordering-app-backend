package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class UpdatePasswordBusinessService {

    @Autowired
    PasswordCryptographyProvider passwordCryptographyProvider;

    @Autowired
    CustomerDao customerDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updatePassword(String authorization, String old_password, String new_password) throws UpdateCustomerException, AuthorizationFailedException {

        if(old_password == null || new_password == null){
            throw new UpdateCustomerException("UCR-003", "No field should be empty");
        }
        int upper_case_count = 0;
        int digit_count = 0;
        int special_case_count =0;
        for (int i = 0; i < new_password.length(); i++) {
            char ch = new_password.charAt(i);
            //to check if the password contains a alphabet
            if (ch >= 'A' && ch <= 'Z') upper_case_count++;
            else if (ch >= '0' && ch <= '9') digit_count++;
            else if(ch == '#' || ch == '@' || ch == '$' || ch == '%' || ch == '&' || ch == '*' || ch == '!' || ch == '^') special_case_count++;
        }

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

        else if(upper_case_count < 1 || digit_count < 1 || special_case_count < 1 || new_password.length() < 8){
            throw new UpdateCustomerException("UCR-001", "Weak password!");
        }
        else if(!passwordCryptographyProvider.encrypt(old_password, customerAuthEntity.getCustomer().getSalt()).equals(customerAuthEntity.getCustomer().getPassword())){
            throw new UpdateCustomerException("UCR-004", "Incorrect old password!");
        }
        else{
            String[] encryptedText = passwordCryptographyProvider.encrypt(new_password);
            customerAuthEntity.getCustomer().setSalt(encryptedText[0]);
            customerAuthEntity.getCustomer().setPassword(encryptedText[1]);
            return customerDao.updateCustomer(customerAuthEntity.getCustomer());
        }
    }
}
