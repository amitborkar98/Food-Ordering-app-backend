package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ItemService {

    @Autowired
    RestaurantDao restaurantDao;

    @Autowired
    CategoryDao categoryDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<ItemEntity> getItemsByCategoryAndRestaurant(String restaurantUUid, String categoryUUid){

        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantById(restaurantUUid);
        List<RestaurantItemEntity> restaurantItemEntities = restaurantEntity.getRestaurantItems();
        List<ItemEntity> restaurantItems = new ArrayList<>();
        for(RestaurantItemEntity s : restaurantItemEntities){
            if(s.getRestaurantEntity() == restaurantEntity){
                restaurantItems.add(s.getItemEntity());
            }
        }
        List<ItemEntity> items = new ArrayList<>();
        CategoryEntity categoryEntity = categoryDao.getCategoryById(categoryUUid);
        List<CategoryItemEntity> categoryItems = categoryEntity.getCategoryItems();
        for(CategoryItemEntity s: categoryItems){
            for(ItemEntity q : restaurantItems){
                if(s.getItemEntity() == q){
                    items.add(q);
                }
            }
        }
        List<String> itemNames = new ArrayList<>();
        for(ItemEntity s : items) {
            itemNames.add(s.getItem_name().toLowerCase());
        }
        Collections.sort(itemNames);
        List<ItemEntity> restaurantCategoryItems = new ArrayList<>();
        for(String s : itemNames){
         for(ItemEntity i : items){
             if(s.equals(i.getItem_name().toLowerCase())){
                 restaurantCategoryItems.add(i);
             }
         }
        }
        List<ItemEntity> newRestaurantCategoryItems = new ArrayList<>();
        for(ItemEntity i : restaurantCategoryItems ){
            for(ItemEntity s : items){
                if(s.getUuid() == i.getUuid()){
                   newRestaurantCategoryItems.add(s);
                }
            }
        }
        return newRestaurantCategoryItems;
    }
}
