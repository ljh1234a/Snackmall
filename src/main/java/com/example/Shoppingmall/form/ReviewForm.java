package com.example.Shoppingmall.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewForm {
    private Integer productId;
    private String content;
    private Integer rating;
    private MultipartFile imgFile;
}
