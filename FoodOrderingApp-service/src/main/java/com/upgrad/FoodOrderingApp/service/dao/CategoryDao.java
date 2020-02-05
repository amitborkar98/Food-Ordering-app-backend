package com.upgrad.FoodOrderingApp.service.dao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class CategoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    public CategoryEntity getCategoryById(final String uuid){
        try {
            return entityManager.createNamedQuery("getCategoryId", CategoryEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<CategoryEntity> getAllCategories(){
        TypedQuery<CategoryEntity> query =entityManager.createQuery("SELECT p from CategoryEntity p order by category_name", CategoryEntity.class);
        return query.getResultList();
    }

}
