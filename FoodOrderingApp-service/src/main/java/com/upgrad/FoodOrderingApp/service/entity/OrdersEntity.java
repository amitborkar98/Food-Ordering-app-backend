package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrdersEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @Size(max = 200)
    private  String uuid;

    @Column(name = "bill")
    private double bill;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "coupon_id")
    CouponEntity couponEntity;

    @Column(name = "discount")
    private double discount;

    @Column(name = "date")
    private Date date;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "customer_id")
    CustomerEntity customerEntity;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "address_id")
    AddressEntity addressEntity;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "restaurant_id")
    RestaurantEntity restaurantEntity;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "payment_id")
    PaymentEntity paymentEntity;

    @OneToMany(mappedBy = "ordersEntity",fetch = FetchType.EAGER)
    List<OrderItemEntity> orderItems = new ArrayList<>();

    public List<OrderItemEntity> getOrderItems(){
        return orderItems;
    }

    public OrdersEntity(String uuid, double bill, CouponEntity couponEntity, double discount, Date date, PaymentEntity paymentEntity,
                        CustomerEntity customerEntity, AddressEntity addressEntity, RestaurantEntity restaurantEntity){
        this.uuid = uuid;
        this.bill = bill;
        this.couponEntity = couponEntity;
        this.discount = discount;
        this.date = date;
        this.paymentEntity = paymentEntity;
        this.customerEntity = customerEntity;
        this.addressEntity = addressEntity;
        this.restaurantEntity = restaurantEntity;
    }

    public CouponEntity getCoupon(){
        return couponEntity;
    }

    public CustomerEntity getCustomer(){
        return customerEntity;
    }

    public RestaurantEntity getRestaurant(){
        return restaurantEntity;
    }

    public PaymentEntity getPayment(){
        return paymentEntity;
    }

    public AddressEntity getAddress(){
        return addressEntity;
    }

    public OrdersEntity(){ }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Number getBill() {
        return bill;
    }

    public void setBill(double bill) {
        this.bill = bill;
    }

    public Number getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
