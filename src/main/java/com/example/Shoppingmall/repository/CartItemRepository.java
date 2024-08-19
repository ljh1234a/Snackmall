package com.example.Shoppingmall.repository;

import com.example.Shoppingmall.entity.CartItem;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends CrudRepository<CartItem, Integer> {
    @Query("SELECT * FROM cart_item WHERE cart_id = :cartId")
    List<CartItem> findByCartId(Integer cartId);

    @Query("SELECT * FROM cart_item WHERE cart_id = :cartId AND product_id = :productId")
    Optional<CartItem> findByCartIdAndProductId(Integer cartId, Integer productId);

    @Modifying
    @Transactional
    @Query("DELETE FROM cart_item WHERE cart_id = :cartId")
    void deleteByCartId(Integer cartId);
}
