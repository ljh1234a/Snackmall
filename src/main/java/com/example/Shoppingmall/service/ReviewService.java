package com.example.Shoppingmall.service;

import com.example.Shoppingmall.entity.Product;
import com.example.Shoppingmall.entity.Review;
import java.util.List;
import java.util.Optional;

public interface ReviewService {
    List<Review> getReviewsByProductId(Integer productId);
    Optional<Product> getProductById(Integer productId);
}
