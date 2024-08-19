package com.example.Shoppingmall.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordForm {
    @NotEmpty
    private String currentPassword;
    @NotEmpty
    private String newPassword;
    @NotEmpty
    private String confirmNewPassword;
}
