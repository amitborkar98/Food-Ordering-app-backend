package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class RestaurantService {

    @Autowired
    RestaurantDao restaurantDao;

    @Autowired
    CategoryDao categoryDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> restaurantsByRating(){
        return restaurantDao.getRestaurants();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity restaurantByUUID(String uuid) throws RestaurantNotFoundException {
        //check if the uuid is null
        if(uuid == null){
            throw new RestaurantNotFoundException("(RNF-002", "Restaurant id field should not be empty");
        }
        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantById(uuid);
        //check if the restaurant is in the database
        if(restaurantEntity == null){
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }
        return restaurantEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity updateRestaurantRating(RestaurantEntity restaurantEntity, double ratings) throws InvalidRatingException {
        //check if the ratings are within given range
        if(ratings < 1.0 || ratings > 5.0){
            throw new InvalidRatingException("IRE-001", "Restaurant should be in the range of 1 to 5");
        }
        //update the ratings of the restaurant
        else {
            DecimalFormat d = new DecimalFormat("#.##");
            Double newRatings = ((restaurantEntity.getCustomer_rating()*restaurantEntity.getNumber_of_customer_rated()) + ratings) /
                    (restaurantEntity.getNumber_of_customer_rated() + 1 );
            restaurantEntity.setCustomerRating(Double.parseDouble(d.format(newRatings)));
            restaurantEntity.setNumberCustomersRated(restaurantEntity.getNumber_of_customer_rated() + 1);
            return restaurantDao.updateRestaurantRatings(restaurantEntity);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> restaurantByCategory(String category_id) throws CategoryNotFoundException {
        //check if the category id is null
        if(category_id == null){
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }
        CategoryEntity categoryEntity = categoryDao.getCategoryById(category_id);
        //check if the categoryEntity is in the database
        if (categoryEntity == null){
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }
        //get the list of all restaurant categories
        List<RestaurantCategoryEntity> restaurantCategoryEntities = categoryEntity.getCategoryRestaurants();
        List<RestaurantEntity> restaurantEntities = new ArrayList<>();
        for(RestaurantCategoryEntity s : restaurantCategoryEntities){
            //add all the restaurants from the restaurant categories list to the restaurant list
            restaurantEntities.add(s.getRestaurantEntity());
        }
        //the code below is used to sort the resturant according to the name
        List<String> restaurantNames = new ArrayList<>();
        for(RestaurantEntity s : restaurantEntities){
            //add the restaurant name in the restaurantNames list
            restaurantNames.add(s.getRestaurant_name().toLowerCase());
        }
        //sort according the name
        Collections.sort(restaurantNames);
        List<RestaurantEntity> newRestaurantEntities = new ArrayList<>();
        //add the restaurant to the newRestaurant list according their name
        for(String s : restaurantNames){
            for(RestaurantEntity i : restaurantEntities){
                if(s.equals(i.getRestaurant_name().toLowerCase())){
                    newRestaurantEntities.add(i);
                }
            }
        }
        return newRestaurantEntities;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> restaurantsByName(String restaurant_name) throws RestaurantNotFoundException {
        //check if the restaurant name is null
        if(restaurant_name == null){
            throw new RestaurantNotFoundException("RNF-003", "Restaurant name field should not be empty");
        }
        return restaurantDao.getRestaurantByName(restaurant_name);
    }
}


