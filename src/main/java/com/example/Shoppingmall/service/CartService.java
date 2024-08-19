package com.example.Shoppingmall.service;

import com.example.Shoppingmall.entity.Cart;
import com.example.Shoppingmall.entity.CartItem;
import com.example.Shoppingmall.entity.User;
import java.util.List;

public interface CartService {
    Cart getCart(Integer userId);
    List<CartItem> getCartItems(Integer cartId);
    void addItemToCart(Integer userId, Integer productId, Integer quantity);
    void removeItemFromCart(Integer userId, Integer itemId);
    void updateCartItemQuantity(Integer itemId, Integer quantity);
    void checkout(Integer cardId, User user);
    void purchaseItem(Integer itemId, User user);
    void clearCart(Integer userId);
}
