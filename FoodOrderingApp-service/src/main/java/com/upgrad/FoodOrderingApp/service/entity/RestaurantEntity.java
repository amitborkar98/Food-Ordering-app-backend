package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "restaurant")
@NamedQueries({
        @NamedQuery(name = "getRestaurantId", query = "select ut from RestaurantEntity ut where ut.uuid =:uuid"),
})
public class RestaurantEntity implements Serializable {

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
    private double customer_rating;

    @Column(name = "average_price_for_two")
    private Integer average_price_for_two;

    @Column(name = "number_of_customers_rated")
    private Integer number_of_customers_rated;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AddressEntity addressEntity;

    @OneToMany(mappedBy = "restaurantEntity" ,fetch = FetchType.LAZY)
    List<RestaurantItemEntity> restaurantItemEntities;

    @OneToMany(mappedBy = "restaurantEntity", fetch = FetchType.LAZY)
    List<OrdersEntity> orders;

    public List<OrdersEntity> getOrders(){
        return orders;
    }

    public List<RestaurantItemEntity> getRestaurantItems(){
        return restaurantItemEntities;
    }

    public void setRestaurantItemEntities(List<RestaurantItemEntity> restaurantItemEntities){
        this.restaurantItemEntities = restaurantItemEntities;
    }

   /* @OneToMany(mappedBy = "categoryEntity" ,fetch = FetchType.LAZY)
    List<RestaurantCategoryEntity> categories;

    public List<RestaurantCategoryEntity> getCategories(){
        return categories;
    }

    public void setCategories(List<RestaurantCategoryEntity> categories){
        this.categories = categories;
    }
    */

    public AddressEntity getAddress(){
        return addressEntity;
    }
    public void setAddress(AddressEntity addressEntity){
        this.addressEntity=addressEntity;
    }

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

    public void setRestaurantName(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhotoUrl(String photo_url) {
        this.photo_url = photo_url;
    }

    public Double getCustomer_rating() {
        return customer_rating;
    }

    public void setCustomerRating(double customer_rating) {
        this.customer_rating = customer_rating;
    }

    public Integer getAverage_price_for_two() {
        return average_price_for_two;
    }

    public void setAvgPrice(Integer average_price_for_two) {
        this.average_price_for_two = average_price_for_two;
    }

    public Integer getNumber_of_customer_rated() {
        return number_of_customers_rated;
    }

    public void setNumberCustomersRated(Integer number_of_customer_rated) {
        this.number_of_customers_rated = number_of_customer_rated;
    }
}
