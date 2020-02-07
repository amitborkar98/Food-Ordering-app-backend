package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import static java.util.Base64.getEncoder;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    PasswordCryptographyProvider passwordCryptographyProvider;

    @Autowired
    CustomerDao customerDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity saveCustomer(CustomerEntity customerEntity)throws SignUpRestrictedException {

        if (customerEntity.getFirstname() == null || customerEntity.getContact_number() == null || customerEntity.getEmail() == null || customerEntity.getPassword() == null) {
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");
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

        if (customerDao.getCustomerByContact(customerEntity.getContact_number()) != null) {
            throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number.");
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


    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity authenticate(String contact_number, String password) throws AuthenticationFailedException {

        if(password == null || contact_number == null){
            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
        }

        if (password.equals("Invalid") ) {
            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
        }

        CustomerEntity customer = customerDao.getCustomerByContact(contact_number);
        if (customer == null) {
            throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
        }

        final String encryptedPassword = passwordCryptographyProvider.encrypt(password, customer.getSalt());
        if (encryptedPassword.equals(customer.getPassword())) {

            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            CustomerAuthEntity customerAuthEntity = new CustomerAuthEntity();
            customerAuthEntity.setCustomer(customer);
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);
            customerAuthEntity.setAccessToken(jwtTokenProvider.generateToken(customer.getUuid(), now, expiresAt));
            customerAuthEntity.setUuid(UUID.randomUUID().toString());
            customerAuthEntity.setLogin_at(now);
            customerAuthEntity.setExpires_at(expiresAt);

            return customerDao.createToken(customerAuthEntity);
        }
        else {
            throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity logout(String authorization) throws AuthorizationFailedException {

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
        else {
            customerAuthEntity.setLogout_at(ZonedDateTime.now());
            customerDao.updateToken(customerAuthEntity);
            return customerAuthEntity;
        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity getCustomer(String authorization) throws AuthorizationFailedException{
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
        else{
            return customerAuthEntity.getCustomer();
        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomer(CustomerEntity customerEntity) throws UpdateCustomerException {
        //not working here
        if(customerEntity.getFirstname() == null){
            throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
        }
        else{
            return customerDao.updateCustomer(customerEntity);
        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomerPassword(String old_password, String new_password, CustomerEntity customerEntity) throws UpdateCustomerException {

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

        if(upper_case_count < 1 || digit_count < 1 || special_case_count < 1 || new_password.length() < 8){
            throw new UpdateCustomerException("UCR-001", "Weak password!");
        }
        else if(!passwordCryptographyProvider.encrypt(old_password, customerEntity.getSalt()).equals(customerEntity.getPassword())){
            throw new UpdateCustomerException("UCR-004", "Incorrect old password!");
        }
        else{
            String[] encryptedText = passwordCryptographyProvider.encrypt(new_password);
            customerEntity.setSalt(encryptedText[0]);
            customerEntity.setPassword(encryptedText[1]);
            return customerDao.updateCustomer(customerEntity);
        }
    }
}
