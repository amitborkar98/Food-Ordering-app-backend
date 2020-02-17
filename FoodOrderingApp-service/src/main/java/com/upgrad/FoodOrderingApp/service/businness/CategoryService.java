package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    RestaurantDao restaurantDao;

    @Autowired
    CategoryDao categoryDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<CategoryEntity> getCategoriesByRestaurant(String uuid){
        //get the restaurantEntity
        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantById(uuid);
        //get the restaurantCategoryEntity
        List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantDao.getAllRestaurantCategories();
        List<CategoryEntity> categories = new ArrayList<>();
        for(RestaurantCategoryEntity s : restaurantCategoryEntities){
            //check if the retaurant in the restaurant category list is same as the given restaurant
            if(s.getRestaurantEntity() == restaurantEntity){
                //add the categories of the restaurant in the category list
                categories.add(s.getCategories());
            }
        }
        return categories;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<CategoryEntity> getAllCategoriesOrderedByName(){
        return categoryDao.getAllCategories();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CategoryEntity getCategoryById(String category_id)throws CategoryNotFoundException {
        //check if the category_id is null
        if(category_id == null){
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }
        CategoryEntity categoryEntity = categoryDao.getCategoryById(category_id);
        //check if the category is the database
        if(categoryEntity == null){
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }
        //get the list of all category items
        List<CategoryItemEntity> categoryItemEntities = categoryEntity.getCategoryItems();
        List<ItemEntity> items = new ArrayList<>();
        for (CategoryItemEntity s : categoryItemEntities){
            //add all the items from the category items list to the items list
            items.add(s.getItemEntity());
        }
        //set the items of the category
        categoryEntity.setItems(items);
        return categoryEntity;
    }
}

