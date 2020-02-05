package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    RestaurantDao restaurantDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> restaurantsByRating(){
        return restaurantDao.getRestaurants();
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity restaurantByUUID(String uuid) throws RestaurantNotFoundException {

        if(uuid == null){
            throw new RestaurantNotFoundException("(RNF-002", "Restaurant id field should not be empty");
        }
        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantById(uuid);
        if(restaurantEntity == null){
            throw new RestaurantNotFoundException("(RNF-001", "No restaurant by this id");
        }
        return restaurantEntity;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity updateRestaurantRating(RestaurantEntity restaurantEntity, double ratings) throws InvalidRatingException {

        if(ratings < 1.0 || ratings > 5.0){
            throw new InvalidRatingException("IRE-001", "Restaurant should be in the range of 1 to 5");
        }
        else {
            DecimalFormat d = new DecimalFormat("#.##");
            Double newRatings = ((restaurantEntity.getCustomer_rating()*restaurantEntity.getNumber_of_customer_rated()) + ratings) /
                    (restaurantEntity.getNumber_of_customer_rated() + 1 );
            restaurantEntity.setCustomer_rating(Double.parseDouble(d.format(newRatings)));
            restaurantEntity.setNumber_of_customer_rated(restaurantEntity.getNumber_of_customer_rated()+1);
            return restaurantDao.updateRestaurantRatings(restaurantEntity);
        }
    }
}


