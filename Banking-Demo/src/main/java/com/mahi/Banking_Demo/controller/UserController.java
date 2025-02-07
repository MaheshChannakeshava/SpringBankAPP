package com.mahi.Banking_Demo.controller;

import com.mahi.Banking_Demo.dto.BankResponse;
import com.mahi.Banking_Demo.dto.UserRequest;
import com.mahi.Banking_Demo.entity.Users;
import com.mahi.Banking_Demo.service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest){

        return userService.createAccount(userRequest);
    }



}
