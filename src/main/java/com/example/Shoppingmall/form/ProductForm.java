package com.example.Shoppingmall.form;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductForm {
    private Integer id;
    @NotNull
    private String name;
    @NotNull
    private Integer price;
    @NotNull
    private Integer stock;
    @NotNull
    private String category;
    @NotNull
    private MultipartFile imgFile;
    private String imageUrl;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
