package com.example.Shoppingmall.service;

import com.example.Shoppingmall.entity.*;
import com.example.Shoppingmall.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductService productService;

    @Autowired
    ShoppingRepository shoppingRepository;

    @Override
    public Cart getCart(Integer userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public List<CartItem> getCartItems(Integer cartId) {
        List<CartItem> items = cartItemRepository.findByCartId(cartId);
        for (CartItem item : items) {
            Optional<Product> productOpt = productRepository.findById(item.getProductId());
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                item.setProductName(product.getName());
                item.setProductImageUrl(product.getImageUrl());
                item.setProductPrice(product.getPrice());
            }
        }
        return items;
    }

    @Override
    public void addItemToCart(Integer userId, Integer productId, Integer quantity) {
        Cart cart = getCart(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cart = cartRepository.save(cart);
        }

        Optional<CartItem> existingCartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);
        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCartId(cart.getId());
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);

            Optional<Product> productOptional = productRepository.findById(productId);
            productOptional.ifPresent(product -> {
                cartItem.setProductName(product.getName());
                cartItem.setProductImageUrl(product.getImageUrl());
                cartItem.setProductPrice(product.getPrice());
            });

            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                cartItem.setUsername(user.getUsername());
            }

            cartItemRepository.save(cartItem);
        }
    }

    @Override
    public void removeItemFromCart(Integer userId, Integer itemId) {
        Cart cart = cartRepository.findByUserId(userId);

        if(cart != null) {
            cartItemRepository.deleteById(itemId);
        }
    }

    @Override
    public void updateCartItemQuantity(Integer itemId, Integer quantity) {
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(itemId);
        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
    }

    @Override
    public void checkout(Integer cartId, User user) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
        List<CartItem> items = cartItemRepository.findByCartId(cartId);

        for (CartItem item : items) {
            Optional<Product> productOptional = productService.getProductById(item.getProductId());
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                if (product.getStock() >= item.getQuantity()) {
                    int totalPrice = product.getPrice() * item.getQuantity();
                    if (user.getMoney() >= totalPrice) {
                        product.setStock(product.getStock() - item.getQuantity());
                        product.setSalesCount(product.getSalesCount() + item.getQuantity());

                        user.setMoney(user.getMoney() - totalPrice);
                        productService.updateProduct(product);
                        userRepository.save(user);

                        ShoppingList shoppingList = new ShoppingList();
                        shoppingList.setUserId(user.getId());
                        shoppingList.setUsername(user.getUsername());
                        shoppingList.setProductId(product.getId());
                        shoppingList.setProductName(product.getName());
                        shoppingList.setQuantity(item.getQuantity());
                        shoppingList.setTotalPrice(totalPrice);
                        shoppingList.setShoppingDate(LocalDateTime.now());
                        shoppingRepository.save(shoppingList);
                    } else {
                        throw new RuntimeException("Insufficient funds");
                    }
                } else {
                    throw new RuntimeException("Insufficient stock");
                }
            }
        }

        cartItemRepository.deleteByCartId(cartId);
    }

    @Override
    public void purchaseItem(Integer itemId, User user) {
        Optional<CartItem> optionalCartItem = cartItemRepository.findById(itemId);
        if (optionalCartItem.isPresent()) {
            CartItem cartItem = optionalCartItem.get();
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            int totalPrice = cartItem.getQuantity() * product.getPrice();
            if (user.getMoney() >= totalPrice) {
                if (product.getStock() >= cartItem.getQuantity()) {
                    product.setStock(product.getStock() - cartItem.getQuantity());
                    product.setSalesCount(product.getSalesCount() + cartItem.getQuantity());

                    productRepository.save(product);

                    user.setMoney(user.getMoney() - totalPrice);
                    userRepository.save(user);

                    ShoppingList shoppingList = new ShoppingList();
                    shoppingList.setUserId(user.getId());
                    shoppingList.setUsername(user.getUsername());
                    shoppingList.setProductId(product.getId());
                    shoppingList.setProductName(product.getName());
                    shoppingList.setQuantity(cartItem.getQuantity());
                    shoppingList.setTotalPrice(totalPrice);
                    shoppingList.setShoppingDate(LocalDateTime.now());
                    shoppingRepository.save(shoppingList);
                    cartItemRepository.delete(cartItem);
                } else {
                    throw new RuntimeException("Insufficient stock");
                }
            } else {
                throw new RuntimeException("Insufficient funds");
            }
        } else {
            throw new RuntimeException("Cart item not found");
        }
    }

    @Override
    public void clearCart(Integer userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart != null) {
            cartItemRepository.deleteByCartId(cart.getId());
        }
    }
}
