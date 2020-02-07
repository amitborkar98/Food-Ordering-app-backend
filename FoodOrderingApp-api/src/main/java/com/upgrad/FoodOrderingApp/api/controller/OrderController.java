package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.*;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/")
public class OrderController {

    @Autowired
    CustomerService customerService;

    @Autowired
    OrderService orderService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    AddressService addressService;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    ItemService itemService;

    @RequestMapping(method = RequestMethod.GET, path = "/order/coupon/{coupon_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCouponByName(@PathVariable("coupon_name") String coupon_name,
                                                    @RequestHeader("authorization") String authorization) throws AuthorizationFailedException,CouponNotFoundException {

        String decode = authorization.split("Bearer ")[1];
        customerService.getCustomer(decode);
        CouponEntity couponEntity = orderService.getCouponByCouponName(coupon_name);
        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse().id(UUID.fromString(couponEntity.getUuid()))
                .couponName(couponEntity.getCoupon_name()).percent(couponEntity.getPercent());
        return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST, path = "/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveOrderResponse> saveOrder(final SaveOrderRequest saveOrderRequest,
                                                       @RequestHeader("authorization") String authorization)
            throws AuthorizationFailedException, RestaurantNotFoundException, CouponNotFoundException, ItemNotFoundException,
            AddressNotFoundException, PaymentMethodNotFoundException {

        String decode = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(decode);
        PaymentEntity paymentEntity = paymentService.getPaymentByUUID(saveOrderRequest.getPaymentId().toString());
        AddressEntity addressEntity = addressService.getAddressByUUID(saveOrderRequest.getAddressId(), customerEntity);
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(saveOrderRequest.getRestaurantId().toString());
        CouponEntity couponEntity = orderService.getCouponByCouponId(saveOrderRequest.getCouponId().toString());

        Date orderDate = new Date();
        Double bill = saveOrderRequest.getBill().doubleValue();
        Double discount = saveOrderRequest.getDiscount().doubleValue();
        OrdersEntity ordersEntity = new OrdersEntity(UUID.randomUUID().toString(), bill, couponEntity, discount ,orderDate ,
                paymentEntity, customerEntity, addressEntity, restaurantEntity);

        List<ItemQuantity> itemQuantities = saveOrderRequest.getItemQuantities();
        List<OrderItemEntity> orderItems = new ArrayList<>();
        for(ItemQuantity i : itemQuantities){
            ItemEntity itemEntity = itemService.getItemById(i.getItemId().toString());
            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setPrice(i.getPrice());
            orderItemEntity.setQuantity(i.getQuantity());
            orderItemEntity.setItemEntity(itemEntity);
            orderItemEntity.setOrdersEntity(ordersEntity);
            orderItems.add(orderItemEntity);
        }
        OrdersEntity savedOrder = orderService.saveOrder(ordersEntity);
        for(OrderItemEntity i : orderItems){
            orderService.saveOrderItem(i);
        }
        SaveOrderResponse saveOrderResponse = new SaveOrderResponse().id(savedOrder.getUuid()).status("ORDER SUCCESSFULLY PLACED");
        return new ResponseEntity<>(saveOrderResponse, HttpStatus.CREATED);
    }


    @RequestMapping(method = RequestMethod.GET, path = "/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<OrderList> getAllOrders(@RequestHeader("authorization") String authorization) throws AuthorizationFailedException{

        String decode = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(decode);

        List<OrdersEntity> ordersEntities = orderService.getOrdersByCustomers(customerEntity.getUuid());
        for(OrdersEntity i : ordersEntities){
            List<OrderItemEntity> orderItems = i.getOrderItems();
            List<ItemEntity> items = new ArrayList<>();
            for(OrderItemEntity s : orderItems){
                items.add(s.getItemEntity());
            }
        }

        OrderList orderList = new OrderList();
        return new ResponseEntity<OrderList>(orderList, HttpStatus.OK);
    }
}
