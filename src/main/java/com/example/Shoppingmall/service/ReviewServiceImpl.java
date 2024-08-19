package com.example.Shoppingmall.service;

import com.example.Shoppingmall.entity.Product;
import com.example.Shoppingmall.entity.Review;
import com.example.Shoppingmall.repository.ProductRepository;
import com.example.Shoppingmall.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public List<Review> getReviewsByProductId(Integer productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public Optional<Product> getProductById(Integer productId) {
        return productRepository.findById(productId);
    }

    public Product getProductForReview(Integer productId) {
        return productRepository.findById(productId).orElse(null);
    }
}
