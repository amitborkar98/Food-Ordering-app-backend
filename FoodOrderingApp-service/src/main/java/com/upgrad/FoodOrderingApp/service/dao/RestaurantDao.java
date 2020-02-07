package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class RestaurantDao {

    @PersistenceContext
    private EntityManager entityManager;


    public List<RestaurantEntity> getRestaurants(){
        TypedQuery<RestaurantEntity> query =entityManager.createQuery("SELECT p from RestaurantEntity p order by customer_rating desc ", RestaurantEntity.class);
        return query.getResultList();
    }

    public RestaurantEntity getRestaurantById(final String uuid){
        try {
            return entityManager.createNamedQuery("getRestaurantId", RestaurantEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<RestaurantCategoryEntity> getAllRestaurantCategories(){
        TypedQuery<RestaurantCategoryEntity> query =entityManager.createQuery("SELECT p from RestaurantCategoryEntity p", RestaurantCategoryEntity.class);
        return query.getResultList();
    }

    public RestaurantEntity updateRestaurantRatings(RestaurantEntity restaurantEntity ){
        return entityManager.merge(restaurantEntity);
    }

    public List<RestaurantEntity> getRestaurantByName(final String restaurant_name1){
        String restaurant_name = restaurant_name1.toLowerCase();
        TypedQuery<RestaurantEntity> query =entityManager.createQuery("SELECT p from RestaurantEntity p where lower(restaurant_name) like concat('%',:restaurant_name,'%') order by restaurant_name",RestaurantEntity.class).setParameter("restaurant_name",restaurant_name);
        return query.getResultList();
    }

}
