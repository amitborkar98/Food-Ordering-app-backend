package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.*;
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

    public List<RestaurantCategoryEntity> getAllCAtegories(){
        TypedQuery<RestaurantCategoryEntity> query =entityManager.createQuery("SELECT p from RestaurantCategoryEntity p", RestaurantCategoryEntity.class);
        return query.getResultList();
    }

}
