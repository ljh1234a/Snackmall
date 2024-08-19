package com.example.Shoppingmall.service;

import com.example.Shoppingmall.entity.Product;
import com.example.Shoppingmall.entity.Review;
import com.example.Shoppingmall.repository.ProductRepository;
import com.example.Shoppingmall.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        averageProductRating();
        return productRepository.findAll(pageable);
    }

    @Override
    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public void insertProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void updateProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public Page<Product> getProductsByCategory(String category, Pageable pageable) {
        return productRepository.findByCategory(category, pageable);
    }

    @Override
    public Page<Product> searchProductsByName(String productName, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(productName, pageable);
    }

    @Override
    public List<Product> getProductByIds(List<Integer> productIds) {
        return productRepository.findAllById(productIds);
    }

    @Override
    public Page<Product> getPopularProducts(Pageable pageable) {
        return productRepository.findBySalesCountGreaterThanEqual(5, pageable);
    }

    @Override
    public void averageProductRating() {
        Iterable<Product> products = productRepository.findAll();
        for (Product product : products) {
            List<Review> reviews = reviewRepository.findByProductId(product.getId());
            if (reviews.size() > 0) {
                float averageRating = (float) reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
                product.setAvrRating(averageRating);
                productRepository.save(product);
            }
        }
    }
}
