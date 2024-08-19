package com.example.Shoppingmall.repository;

import com.example.Shoppingmall.entity.ShoppingList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ShoppingRepository extends PagingAndSortingRepository<ShoppingList, Integer>, CrudRepository<ShoppingList, Integer> {
    Page<ShoppingList> findByUserId(Integer userId, Pageable pageable);
}
