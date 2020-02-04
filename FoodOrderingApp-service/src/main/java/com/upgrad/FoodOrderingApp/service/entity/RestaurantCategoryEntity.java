package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant_category")
public class RestaurantCategoryEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    RestaurantEntity restaurantEntity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CategoryEntity categoryEntity;

    public RestaurantEntity getRestaurantEntity(){
        return restaurantEntity;
    }

    public void setRestaurantEntity(RestaurantEntity restaurantEntity){
        this.restaurantEntity = restaurantEntity;
    }

    public CategoryEntity getCategories(){
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity){
        this.categoryEntity = categoryEntity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
