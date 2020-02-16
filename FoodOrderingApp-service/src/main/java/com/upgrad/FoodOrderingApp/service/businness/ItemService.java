package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.ItemDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ItemService {

    @Autowired
    ItemDao itemDao;

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
        return restaurantCategoryItems;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<ItemEntity> getItemsByPopularity(RestaurantEntity restaurantEntity){

        List<OrdersEntity> orders = restaurantEntity.getOrders();

        List<ItemEntity> items = new ArrayList<>();
        for(OrdersEntity s : orders){
            List<OrderItemEntity> orderItems = s.getOrderItems();
            for(OrderItemEntity i : orderItems){
                items.add(i.getItemEntity());
            }
        }

        List<RestaurantItemEntity> restaurantItemEntities = restaurantEntity.getRestaurantItems();
        List<ItemEntity> itemEntities = new ArrayList<>();
        for(RestaurantItemEntity s : restaurantItemEntities){
            itemEntities.add(s.getItemEntity());
        }

        for(ItemEntity i : itemEntities){
            for(ItemEntity s : items){
                if(s == i){
                    i.setOrderCount(i.getOrderCount() + 1);
                }
            }
        }

        List<Integer> itemCounts = new ArrayList<>();
        for(ItemEntity i : itemEntities){
            if(i.getOrderCount() != 0){
                itemCounts.add(i.getOrderCount());
            }
        }
        Collections.sort(itemCounts);
        Collections.reverse(itemCounts);

        List<ItemEntity> newItems = new ArrayList<>();
        for(Integer i : itemCounts){
            for(ItemEntity s : itemEntities){
                if(i == s.getOrderCount()){
                    if(!newItems.contains(s)){
                        newItems.add((s));
                    }
                }
            }
        }

        return newItems.subList(0,5);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ItemEntity getItemById(String item_id) throws ItemNotFoundException {
        ItemEntity itemEntity = itemDao.getItemById(item_id);
        if(itemEntity == null){
            throw new ItemNotFoundException("INF-003", "No item by this id exist");
        }
        return itemEntity;
    }
}
