package com.example.Shoppingmall.repository;

import com.example.Shoppingmall.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Integer>, CrudRepository<User, Integer> {
    User findByLoginId(String loginId);
}
