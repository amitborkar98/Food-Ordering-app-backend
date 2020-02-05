package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "category")
@NamedQueries({
        @NamedQuery(name = "getCategoryId", query = "select ut from CategoryEntity ut where ut.uuid =:uuid")
})
public class CategoryEntity implements Serializable {

        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(name = "uuid")
        @Size(max = 200)
        private  String uuid;

        @Column(name = "category_name")
        @Size(max = 255)
        private String category_name;

        @OneToMany(mappedBy = "categoryEntity",fetch = FetchType.EAGER)
        List<CategoryItemEntity> categoryItems;


    public List<CategoryItemEntity> getCategoryItems(){
        return categoryItems;
    }

    public void setCategoryItems(List<CategoryItemEntity> categoryItems){
        this.categoryItems = categoryItems;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

}
