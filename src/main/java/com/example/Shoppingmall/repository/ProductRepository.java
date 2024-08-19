package com.example.Shoppingmall.repository;

import com.example.Shoppingmall.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>, CrudRepository<Product, Integer> {
    Page<Product> findByCategory(String category, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase(String productsName, Pageable pageable);
    List<Product> findAllById(Iterable<Integer> ids);
    Page<Product> findBySalesCountGreaterThanEqual(int salesCount, Pageable pageable);
}
