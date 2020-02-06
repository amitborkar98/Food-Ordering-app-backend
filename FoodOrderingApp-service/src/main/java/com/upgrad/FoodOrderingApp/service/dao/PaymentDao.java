package com.upgrad.FoodOrderingApp.service.dao;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class PaymentDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<PaymentEntity> getAllPayments(){
        TypedQuery<PaymentEntity> query =entityManager.createQuery("SELECT p from PaymentEntity p", PaymentEntity.class);
        return query.getResultList();
    }

    public PaymentEntity getPaymentById(String uuid){
        try {
            return entityManager.createNamedQuery("getPaymentById", PaymentEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
