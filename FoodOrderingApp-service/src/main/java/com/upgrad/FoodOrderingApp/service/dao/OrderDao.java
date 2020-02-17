package com.upgrad.FoodOrderingApp.service.dao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrdersEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderDao {

    @PersistenceContext
    private EntityManager entityManager;

    //get couponEntity by coupon_name
    public CouponEntity getCouponByName(String coupon_name){
        try {
            return entityManager.createNamedQuery("getCouponByName", CouponEntity.class).setParameter("coupon_name", coupon_name).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //get couponEntity by coupon_name
    public CouponEntity getCouponById(String uuid){
        try {
            return entityManager.createNamedQuery("getCouponById", CouponEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //persist the orderEntity in the database
    public OrdersEntity saveOrder(OrdersEntity ordersEntity){
        entityManager.persist(ordersEntity);
        return ordersEntity;
    }

    //persist the orderItemEntity in the database
    public OrderItemEntity saveOrderItem(OrderItemEntity orderItemEntity){
        entityManager.persist(orderItemEntity);
        return orderItemEntity;
    }
}
