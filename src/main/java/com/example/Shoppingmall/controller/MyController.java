package com.example.Shoppingmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class MyController {
    @GetMapping
    public String selfIntroduce() {
        return "selfIntroduce";
    }
}
