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
@Table("product")
public class Product {
    @Id
    private Integer id;
    private String name;
    private Integer price;
    private Integer stock;
    private String category;
    private String imageUrl;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer salesCount;
    private Float avrRating;
}
