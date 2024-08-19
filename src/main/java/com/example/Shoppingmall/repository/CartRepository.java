package com.example.Shoppingmall.repository;

import com.example.Shoppingmall.entity.Cart;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart, Integer> {
    Cart findByUserId(Integer userId);
}
