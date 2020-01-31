package com.upgrad.FoodOrderingApp.service.entity;

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
    AddressEntity addressEntity;

}
