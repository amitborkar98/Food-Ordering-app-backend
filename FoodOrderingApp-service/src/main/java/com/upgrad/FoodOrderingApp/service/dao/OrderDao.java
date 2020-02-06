package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class OrderDao {

    @PersistenceContext
    private EntityManager entityManager;

    public CouponEntity getCouponByName(String coupon_name){
        try {
            return entityManager.createNamedQuery("getCouponByName", CouponEntity.class).setParameter("coupon_name", coupon_name).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}
