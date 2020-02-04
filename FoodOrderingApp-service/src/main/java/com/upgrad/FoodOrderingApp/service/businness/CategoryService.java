package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    RestaurantDao restaurantDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<CategoryEntity> getCategoriesByRestaurant(String uuid){

        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantById(uuid);
        List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantDao.getAllCAtegories();
        List<CategoryEntity> categories = new ArrayList<>();
        for(RestaurantCategoryEntity s : restaurantCategoryEntities){
            if(s.getRestaurantEntity() == restaurantEntity){
                categories.add(s.getCategories());
            }
        }
        return categories;
    }
}
