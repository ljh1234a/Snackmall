package com.example.Shoppingmall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("shop_user")
public class User {
    @Id
    private Integer id;
    private String username;
    private String userId;
    private String password;
    private String confirmPassword;
    private String email;
    private String tel;
    private Integer money;
    private String role;
}
