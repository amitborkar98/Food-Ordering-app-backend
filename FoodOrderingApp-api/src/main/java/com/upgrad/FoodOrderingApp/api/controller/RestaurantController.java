package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
@CrossOrigin
public class RestaurantController {

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    ItemService itemService;

    @Autowired
    CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurants(){

        List<RestaurantEntity> restaurantEntities = restaurantService.restaurantsByRating();
        List<RestaurantList> restaurantLists = new ArrayList<>();


        for(RestaurantEntity s : restaurantEntities){

            RestaurantList restaurantList = new RestaurantList();
            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState();

            restaurantDetailsResponseAddress.setId(UUID.fromString(s.getAddress().getUuid()));
            restaurantDetailsResponseAddress.setCity(s.getAddress().getCity());
            restaurantDetailsResponseAddress.setFlatBuildingName(s.getAddress().getFlat_buil_number());
            restaurantDetailsResponseAddress.setLocality(s.getAddress().getLocality());
            restaurantDetailsResponseAddress.setPincode(s.getAddress().getPincode());

            restaurantDetailsResponseAddressState.setId(UUID.fromString(s.getAddress().getState().getUuid()));
            restaurantDetailsResponseAddressState.setStateName(s.getAddress().getState().getState_name());
            restaurantDetailsResponseAddress.setState(restaurantDetailsResponseAddressState);

            List<CategoryEntity> categoryEntities = categoryService.getCategoriesByRestaurant(s.getUuid());

            restaurantList.setId(UUID.fromString(s.getUuid()));
            restaurantList.setRestaurantName(s.getRestaurant_name());
            restaurantList.setPhotoURL(s.getPhoto_url());
            restaurantList.setAveragePrice(s.getAverage_price_for_two());
            restaurantList.setAddress(restaurantDetailsResponseAddress);
            restaurantList.setNumberCustomersRated(s.getNumber_of_customer_rated());

            BigDecimal customer_ratings = new BigDecimal(s.getCustomer_rating().toString());
            restaurantList.setCustomerRating(customer_ratings);

            List<String> category_names = new ArrayList<>();
            for(CategoryEntity o : categoryEntities){
              category_names.add(o.getCategory_name());
            }

            Collections.sort(category_names);

            StringBuilder sb = new StringBuilder();
            String comma = ",";
            String delim = " ";
            int i=0;
            while (i< category_names.size() - 1){
                sb.append(category_names.get(i));
                sb.append(comma);
                sb.append(delim);
                i++;
            }
            sb.append(category_names.get(i));
            String res = sb.toString();

            restaurantList.setCategories(res);

            restaurantLists.add(restaurantList);
        }
        RestaurantListResponse restaurantListResponse = new RestaurantListResponse().restaurants(restaurantLists);
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, path = "/api/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantDetailsResponse> getRestaurantById(@PathVariable("restaurant_id") final String restaurant_id) throws RestaurantNotFoundException {

        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurant_id);
        List<CategoryEntity> categoryEntities = categoryService.getCategoriesByRestaurant(restaurantEntity.getUuid());

        List<CategoryList> categoryLists = new ArrayList<>();
        for(CategoryEntity s : categoryEntities){
            List<ItemEntity> itemEntities = itemService.getItemsByCategoryAndRestaurant(restaurantEntity.getUuid(), s.getUuid());

            List<ItemList> itemLists = new ArrayList<>();
            for(ItemEntity o : itemEntities){
                if(o.getType().equals("0")){
                    ItemList.ItemTypeEnum typeEnum = ItemList.ItemTypeEnum.valueOf("VEG");
                    ItemList itemList = new ItemList().id(UUID.fromString(o.getUuid())).itemName(o.getItem_name()).price(o.getPrice())
                            .itemType(typeEnum);
                    itemLists.add(itemList);
                }
                else{
                    ItemList.ItemTypeEnum typeEnum = ItemList.ItemTypeEnum.valueOf("NON_VEG");
                    ItemList itemList = new ItemList().id(UUID.fromString(o.getUuid())).itemName(o.getItem_name()).price(o.getPrice())
                            .itemType(typeEnum);
                    itemLists.add(itemList);
                }
            }
            CategoryList categoryList = new CategoryList().id(UUID.fromString(s.getUuid())).categoryName(s.getCategory_name())
                    .itemList(itemLists);
            categoryLists.add(categoryList);
        }
        //sorting the list
        List<String> categoryNames = new ArrayList<>();
        for(CategoryList i : categoryLists){
            categoryNames.add(i.getCategoryName());
        }
        Collections.sort(categoryNames);
        List<CategoryList> newCategoryList = new ArrayList<>();
        for(String s : categoryNames){
            for(CategoryList i : categoryLists){
                if(s.equals(i.getCategoryName())){
                    newCategoryList.add(i);
                }
            }
        }


        RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                .id(UUID.fromString(restaurantEntity.getAddress().getUuid())).stateName(restaurantEntity.getAddress().getState().getState_name());
        RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress().id(UUID.fromString(restaurantEntity.getAddress().getUuid()))
                .city(restaurantEntity.getAddress().getCity()).flatBuildingName(restaurantEntity.getAddress().getFlat_buil_number())
                .locality(restaurantEntity.getAddress().getLocality()).pincode(restaurantEntity.getAddress().getPincode())
                .state(restaurantDetailsResponseAddressState);

        BigDecimal customer_ratings = new BigDecimal(restaurantEntity.getCustomer_rating().toString());
        RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse().id(UUID.fromString(restaurantEntity.getUuid()))
                .restaurantName(restaurantEntity.getRestaurant_name()).averagePrice(restaurantEntity.getAverage_price_for_two()).address(restaurantDetailsResponseAddress)
                .photoURL(restaurantEntity.getPhoto_url()).customerRating(customer_ratings).categories(newCategoryList)
                .numberCustomersRated(restaurantEntity.getNumber_of_customer_rated());

        return new ResponseEntity<RestaurantDetailsResponse>(restaurantDetailsResponse, HttpStatus.OK);
    }
}
