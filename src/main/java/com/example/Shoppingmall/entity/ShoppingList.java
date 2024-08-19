package com.example.Shoppingmall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("shopping_list")
public class ShoppingList {
    @Id
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer quantity;
    private Integer totalPrice;
    private LocalDateTime shoppingDate;
    private String username;
    private String productName;
}
