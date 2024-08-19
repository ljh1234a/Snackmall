package com.example.Shoppingmall.repository;

import com.example.Shoppingmall.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
    List<Review> findByProductId(Integer productId);
    Page<Review> findAll(Pageable pageable);
}
