package com.example.Shoppingmall.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {
    private Integer id;
    @NotBlank
    private String username;
    @NotBlank
    private String loginId;
    @NotBlank
    private String password;
    @NotBlank
    private String confirmPassword;
    private String email;
    private String emailCode;
    private String tel;
    private Integer money;
    private String role;

}
