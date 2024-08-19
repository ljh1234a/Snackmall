package com.example.Shoppingmall.form;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeBoardForm {
    private Integer id;
    @NotNull
    private String title;
    @NotNull
    private String content;
    private Integer userId;
    private String username;
    private LocalDateTime createDate;
    private Integer views;
}
