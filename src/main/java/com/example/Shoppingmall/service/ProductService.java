package com.example.Shoppingmall.service;

import com.example.Shoppingmall.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    Page<Product> getAllProducts(Pageable pageable);
    Optional<Product> getProductById(Integer id);
    void insertProduct(Product product);
    void updateProduct(Product product);
    void deleteProduct(Integer id);
    void saveProduct(Product product);
    Page<Product> getProductsByCategory(String category, Pageable pageable);
    Page<Product> searchProductsByName(String productName, Pageable pageable);
    List<Product> getProductByIds(List<Integer> productIds);
    Page<Product> getPopularProducts(Pageable pageable);
    void averageProductRating();
}
