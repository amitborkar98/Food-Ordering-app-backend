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
        //get restaurant by id
        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantById(restaurantUUid);
        // get the list of restaurantItems
        List<RestaurantItemEntity> restaurantItemEntities = restaurantEntity.getRestaurantItems();
        List<ItemEntity> restaurantItems = new ArrayList<>();
        for(RestaurantItemEntity s : restaurantItemEntities){
            if(s.getRestaurantEntity() == restaurantEntity){
                //add all the items from the RestaurantItemEntities list to the restaurantItems list
                restaurantItems.add(s.getItemEntity());
            }
        }
        List<ItemEntity> items = new ArrayList<>();
        //get category by id
        CategoryEntity categoryEntity = categoryDao.getCategoryById(categoryUUid);
        //get the list of  category items
        List<CategoryItemEntity> categoryItems = categoryEntity.getCategoryItems();
        for(CategoryItemEntity s: categoryItems){
            for(ItemEntity q : restaurantItems){
                //if the item in the categoryItems is same as the item in the restaurantItems, add it the new items list
                if(s.getItemEntity() == q){
                    items.add(q);
                }
            }
        }
        //below code is to sort the items according to their name
        List<String> itemNames = new ArrayList<>();
        for(ItemEntity s : items) {
            //add the item_name from the items list
            itemNames.add(s.getItem_name().toLowerCase());
        }
        //sort the list
        Collections.sort(itemNames);
        //add all the items according to their name
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
        // get the list of all orders of a restaurant
        List<OrdersEntity> orders = restaurantEntity.getOrders();
        List<ItemEntity> items = new ArrayList<>();
        for(OrdersEntity s : orders){
            //get the list of orderItems
            List<OrderItemEntity> orderItems = s.getOrderItems();
            for(OrderItemEntity i : orderItems){
                //add the items from the orderItems list to the items list
                items.add(i.getItemEntity());
            }
        }
        //get the list of restaurantItems
        List<RestaurantItemEntity> restaurantItemEntities = restaurantEntity.getRestaurantItems();
        List<ItemEntity> itemEntities = new ArrayList<>();
        for(RestaurantItemEntity s : restaurantItemEntities){
            //add the items from the restaurantItems to the itemsEntities list
            itemEntities.add(s.getItemEntity());
        }
        //check if the items are in the ordered items, if they are, increment the orderCount by 1
        for(ItemEntity i : itemEntities){
            for(ItemEntity s : items){
                if(s == i){
                    i.setOrderCount(i.getOrderCount() + 1);
                }
            }
        }
        //add the orderCount in the new list for sorting
        List<Integer> itemCounts = new ArrayList<>();
        for(ItemEntity i : itemEntities){
            itemCounts.add(i.getOrderCount());
        }
        //sort the items counts
        Collections.sort(itemCounts);
        //reverse the item counts
        Collections.reverse(itemCounts);
        List<ItemEntity> newItems = new ArrayList<>();
        // add the items according to their orderCount
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
        //check if the item is in the database
        if(itemEntity == null){
            throw new ItemNotFoundException("INF-003", "No item by this id exist");
        }
        return itemEntity;
    }
}
