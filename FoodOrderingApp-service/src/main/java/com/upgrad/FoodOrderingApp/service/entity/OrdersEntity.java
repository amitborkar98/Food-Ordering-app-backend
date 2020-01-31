package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

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
    private Number bill;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "coupon_id")
    CouponEntity couponEntity;

    @Column(name = "discount")
    private Number discount;

    @Column(name = "date")
    private ZonedDateTime date;

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

    public void setBill(Number bill) {
        this.bill = bill;
    }

    public Number getDiscount() {
        return discount;
    }

    public void setDiscount(Number discount) {
        this.discount = discount;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
}
