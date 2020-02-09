package com.upgrad.FoodOrderingApp.api.controller;
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.api.model.ItemListResponse;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/")
public class ItemController {

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    ItemService itemService;

    @RequestMapping(method = RequestMethod.GET, path = "/item/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ItemListResponse> getItems(@PathVariable("restaurant_id") String restaurant_id) throws RestaurantNotFoundException {
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurant_id);
        List<ItemEntity> itemEntities = itemService.getItemsByPopularity(restaurantEntity);
        List<ItemList> itemLists = new ArrayList<>();
        for(ItemEntity i : itemEntities){
            if(i.getType().equals("0")){
                ItemList.ItemTypeEnum typeEnum = ItemList.ItemTypeEnum.valueOf("VEG");
                ItemList itemList = new ItemList().id(UUID.fromString(i.getUuid())).itemName(i.getItem_name())
                        .price(i.getPrice()).itemType(typeEnum);
                itemLists.add(itemList);
            }
            else {
                ItemList.ItemTypeEnum typeEnum = ItemList.ItemTypeEnum.valueOf("NON_VEG");
                ItemList itemList = new ItemList().id(UUID.fromString(i.getUuid())).itemName(i.getItem_name())
                        .price(i.getPrice()).itemType(typeEnum);
                itemLists.add(itemList);
            }
        }
        ItemListResponse itemListResponse = new ItemListResponse();
        for(ItemList i : itemLists){
            itemListResponse.add(i);
        }
        return new ResponseEntity<ItemListResponse>(itemListResponse, HttpStatus.OK);
    }
}
