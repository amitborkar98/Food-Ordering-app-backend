package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignUpBusinessService {

    @Autowired
    PasswordCryptographyProvider passwordCryptographyProvider;

    @Autowired
    CustomerDao customerDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity signupCustomer(CustomerEntity customerEntity)throws SignUpRestrictedException {


        if (customerDao.getCustomerByContact(customerEntity.getContact_number()) != null) {
            throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number.");
        }
        if (customerEntity.getFirstname() == null || customerEntity.getContact_number() == null || customerEntity.getEmail() == null || customerEntity.getPassword() == null) {
            throw new SignUpRestrictedException("(SGR-005", "Except last name all fields should be filled");
        }
        String email = customerEntity.getEmail();
        int a_count = 0;
        int charCount = 0;
        int dot_count = 0;
        for (int i = 0; i < email.length(); i++) {
            char ch = email.charAt(i);
            ch = Character.toUpperCase(ch);
            //to check if the password contains a digit or alphabet
            if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'Z')) charCount++;
            else if (ch == '.') dot_count++;
            else if (ch == '@') a_count++;
        }

        String contact = customerEntity.getContact_number();
        int not_number_count = 0;
        for (int i = 0; i < contact.length(); i++) {
            char ch = contact.charAt(i);
            ch = Character.toUpperCase(ch);
            //to check if the password contains a alphabet
            if ((ch >= 'A' && ch <= 'Z')) not_number_count++;
        }

        String password = customerEntity.getPassword();
        int upper_case_count = 0;
        int digit_count = 0;
        int special_case_count =0;
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            //to check if the password contains a alphabet
            if (ch >= 'A' && ch <= 'Z') upper_case_count++;
            else if (ch >= '0' && ch <= '9') digit_count++;
            else if(ch == '#' || ch == '@' || ch == '$' || ch == '%' || ch == '&' || ch == '*' || ch == '!' || ch == '^') special_case_count++;
        }

        if (charCount < 1 || dot_count < 1 || a_count < 1){
            throw new SignUpRestrictedException("(SGR-002", "Invalid email-id format!");
        }
        else if(not_number_count > 0 || contact.length() != 10 ){
            throw new SignUpRestrictedException("(SGR-003", "Invalid contact number!");
        }
        else if(upper_case_count < 1 || digit_count < 1 || special_case_count < 1 || password.length() < 8){
            throw new SignUpRestrictedException("SGR-004", "Weak password!");
        }
        else {
            String[] encryptedText = passwordCryptographyProvider.encrypt(customerEntity.getPassword());
            customerEntity.setSalt(encryptedText[0]);
            customerEntity.setPassword(encryptedText[1]);
            return customerDao.createCustomer(customerEntity);
        }
    }
}
