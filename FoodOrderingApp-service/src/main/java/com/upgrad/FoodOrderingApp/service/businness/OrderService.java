package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrdersEntity;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.security.sasl.SaslServer;
import java.util.Collections;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    CustomerDao customerDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public CouponEntity getCouponByCouponName(String coupon_name) throws CouponNotFoundException{
        //check if the coupon_name is null
        if(coupon_name == null){
            throw new CouponNotFoundException("CPF-002", "Coupon name field should not be empty");
        }
        CouponEntity couponEntity = orderDao.getCouponByName(coupon_name);
        //check if the coupon is in the database
        if(couponEntity == null){
            throw new CouponNotFoundException("CPF-001", "No coupon by this name");
        }
        return couponEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CouponEntity getCouponByCouponId(String coupon_id) throws  CouponNotFoundException{
        CouponEntity couponEntity = orderDao.getCouponById(coupon_id);
        //check if the coupon is in the database
        if(couponEntity == null){
            throw new CouponNotFoundException("CPF-002", "No coupon by this id");
        }
        return couponEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public OrdersEntity saveOrder(OrdersEntity ordersEntity){
        return orderDao.saveOrder(ordersEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public OrderItemEntity saveOrderItem(OrderItemEntity orderItemEntity){
        return orderDao.saveOrderItem(orderItemEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<OrdersEntity> getOrdersByCustomers(String customer_id){
        //get the customer by id
        CustomerEntity customerEntity = customerDao.getCustomerById(customer_id);
        //get all the orders of that customer
        List<OrdersEntity> orders = customerEntity.getCustomerOrders();
        //reverse the order list to get the latest order first
        Collections.reverse(orders);
        return orders;
    }
}
