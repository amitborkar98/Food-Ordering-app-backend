package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
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


    @Transactional(propagation = Propagation.REQUIRED)
    public List<CategoryEntity> getAllCategoriesOrderedByName(){
        return categoryDao.getAllCategories();
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public CategoryEntity getCategoryById(String category_id)throws CategoryNotFoundException {
        if(category_id == null){
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }
        CategoryEntity categoryEntity = categoryDao.getCategoryById(category_id);
        if(categoryEntity == null){
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }
        return categoryEntity;
    }
}

