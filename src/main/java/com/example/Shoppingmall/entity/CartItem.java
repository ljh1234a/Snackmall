package com.example.Shoppingmall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("cart_item")
public class CartItem {
    @Id
    private Integer id;
    private Integer cartId;
    private Integer productId;
    private Integer quantity;
    private String username;
    private String productName;
    private String productImageUrl;
    private Integer productPrice;
}
