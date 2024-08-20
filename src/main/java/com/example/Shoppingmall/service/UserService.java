package com.example.Shoppingmall.service;

import com.example.Shoppingmall.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<User> getAllUsers(Pageable pageable);
    User findByLoginId(String loginId);
    void save(User user);
}
