package com.example.Shoppingmall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    JavaMailSender mailSender;

    @Override
    public void sendEmailCode(String toEmail, String emailCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Snack Mall 회원가입 인증번호는 " + emailCode + " 입니다.");
        message.setText("인증번호: " + emailCode);
        mailSender.send(message);
    }
}
