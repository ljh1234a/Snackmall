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
@Table("review")
public class Review {
    @Id
    private Integer id;
    private Integer productId;
    private Integer userId;
    private String username;
    private String productName;
    private String content;
    private Integer rating;
    private LocalDateTime createDate;
    private String imageUrl;
}
