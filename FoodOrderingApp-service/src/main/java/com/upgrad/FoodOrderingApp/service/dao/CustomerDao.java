package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerDao {

    @PersistenceContext
    private EntityManager entityManager;

    //persist the customerEntity in database
    public CustomerEntity createCustomer(CustomerEntity customerEntity){
        entityManager.persist(customerEntity);
        return customerEntity;
    }

    //merge/update the customerEntity in database
    public CustomerEntity updateCustomer(CustomerEntity customerEntity){
        return entityManager.merge(customerEntity);
    }

    //get the customerEntity by contact_no from the database
    public CustomerEntity getCustomerByContact(final String contact_number){
        try {
            return entityManager.createNamedQuery("customerByContact", CustomerEntity.class).setParameter("contact_number", contact_number).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //get the customerEntity by uuid from the database
    public CustomerEntity getCustomerById(final String uuid){
        try {
            return entityManager.createNamedQuery("customerById", CustomerEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //persist the customerAuthEntity in  database
    public CustomerAuthEntity createToken(final CustomerAuthEntity customerAuthEntity) {
        entityManager.persist(customerAuthEntity);
        return customerAuthEntity;
    }

    //merge/update the customerAuthEntity in database
    public void updateToken (final CustomerAuthEntity customerAuthEntity){
        entityManager.merge(customerAuthEntity);
    }

    //get the customerAuthEntity by access-token from the database
    public CustomerAuthEntity getCustomerAuth(final String access_token) {
        try {
            return entityManager.createNamedQuery("getCustomerToken", CustomerAuthEntity.class).setParameter("access_token", access_token).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}
