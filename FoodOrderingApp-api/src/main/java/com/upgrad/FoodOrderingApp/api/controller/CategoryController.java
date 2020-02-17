package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CategoriesListResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
@CrossOrigin
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET, path = "/category", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoriesListResponse> getCategories(){
        List<CategoryEntity> categoryEntities = categoryService.getAllCategoriesOrderedByName();
        //if categories size is 0 return null
        if(categoryEntities.size() == 0){
            CategoriesListResponse categoriesListResponse = new CategoriesListResponse().categories(null);
            return new ResponseEntity<CategoriesListResponse>(categoriesListResponse, HttpStatus.OK);
        }
        List<CategoryListResponse> categoryListResponses = new ArrayList<>();
        for(CategoryEntity s : categoryEntities){
            CategoryListResponse categoryListResponse = new CategoryListResponse().id(UUID.fromString(s.getUuid()))
                    .categoryName(s.getCategory_name());
            categoryListResponses.add(categoryListResponse);
        }
        CategoriesListResponse categoriesListResponse = new CategoriesListResponse().categories(categoryListResponses);
        return new ResponseEntity<CategoriesListResponse>(categoriesListResponse, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, path = "/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoryDetailsResponse> getCategoryById(@RequestBody(required = false) @PathVariable("category_id") String category_id) throws CategoryNotFoundException {
        CategoryEntity categoryEntity = categoryService.getCategoryById(category_id);
        List<ItemEntity> items = categoryEntity.getItems();
        List<ItemList> itemLists = new ArrayList<>();
        for(ItemEntity i : items){
            //if item type is 0
            if(i.getType().equals("0")){
                ItemList.ItemTypeEnum typeEnum = ItemList.ItemTypeEnum.valueOf("VEG");
                ItemList itemList = new ItemList().id(UUID.fromString(i.getUuid())).price(i.getPrice())
                        .itemName(i.getItem_name()).itemType(typeEnum);
                itemLists.add(itemList);
            }
            //if item type is 1
            else {
                ItemList.ItemTypeEnum typeEnum = ItemList.ItemTypeEnum.valueOf("NON_VEG");
                ItemList itemList = new ItemList().id(UUID.fromString(i.getUuid())).price(i.getPrice())
                        .itemName(i.getItem_name()).itemType(typeEnum);
                itemLists.add(itemList);
            }
        }
        CategoryDetailsResponse categoryDetailsResponse = new CategoryDetailsResponse().id(UUID.fromString(categoryEntity.getUuid()))
                .categoryName(categoryEntity.getCategory_name()).itemList(itemLists);
        return new ResponseEntity<CategoryDetailsResponse>(categoryDetailsResponse, HttpStatus.OK);
    }
}
