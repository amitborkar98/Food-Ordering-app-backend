package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class AddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    public StateEntity getStateById(final String uuid){
        try {
            return entityManager.createNamedQuery("getStateById", StateEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public AddressEntity getAddressById(final String uuid){
        try {
            return entityManager.createNamedQuery("getAdressId", AddressEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public AddressEntity saveAdress(AddressEntity addressEntity){
        entityManager.persist(addressEntity);
        return addressEntity;
    }

    public void createCustomerAdress(CustomerAddressEntity customerAddressEntity){
        entityManager.persist(customerAddressEntity);
    }

    public List<AddressEntity> getAddress(){
        TypedQuery<AddressEntity> query =entityManager.createQuery("SELECT p from AddressEntity p", AddressEntity.class);
        return query.getResultList();
    }

    public List<StateEntity> getAllStates(){
        TypedQuery<StateEntity> query =entityManager.createQuery("SELECT p from StateEntity p", StateEntity.class);
        return query.getResultList();
    }

    public List<CustomerAddressEntity> getAllCustomerAddress(){
        TypedQuery<CustomerAddressEntity> query =entityManager.createQuery("SELECT p from CustomerAddressEntity p", CustomerAddressEntity.class);
        return query.getResultList();
    }

    public void deleteAddress(AddressEntity addressEntity){
        entityManager.remove(addressEntity);
    }

    public AddressEntity updateAddress(AddressEntity addressEntity){
        return entityManager.merge(addressEntity);
    }
}
