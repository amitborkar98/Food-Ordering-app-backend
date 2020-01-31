package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "restaurant")
public class RestaurantEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @Size(max = 200)
    private  String uuid;

    @Column(name = "restaurant_name")
    @Size(max = 50)
    private String restaurant_name;

    @Column(name = "photo_url")
    @Size(max = 255)
    private String photo_url;

    @Column(name = "customer_rating")
    private Number customer_rating;

    @Column(name = "average_price_for_two")
    private Integer average_price_for_two;

    @Column(name = "number_of_customer_rated")
    private Integer number_of_customer_rated;

    @OneToOne
    @JoinColumn(name = "address_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    AddressEntity addressEntity;

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

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public Number getCustomer_rating() {
        return customer_rating;
    }

    public void setCustomer_rating(Number customer_rating) {
        this.customer_rating = customer_rating;
    }

    public Integer getAverage_price_for_two() {
        return average_price_for_two;
    }

    public void setAverage_price_for_two(Integer average_price_for_two) {
        this.average_price_for_two = average_price_for_two;
    }

    public Integer getNumber_of_customer_rated() {
        return number_of_customer_rated;
    }

    public void setNumber_of_customer_rated(Integer number_of_customer_rated) {
        this.number_of_customer_rated = number_of_customer_rated;
    }
}
