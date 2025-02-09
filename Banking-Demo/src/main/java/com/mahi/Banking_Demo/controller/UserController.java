package com.mahi.Banking_Demo.controller;

import com.mahi.Banking_Demo.dto.*;
import com.mahi.Banking_Demo.entity.Transactions;
import com.mahi.Banking_Demo.entity.Users;
import com.mahi.Banking_Demo.service.BankStatement;
import com.mahi.Banking_Demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "User Account Managment API's")
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

//    @Autowired
//    BankStatement bankStatement;
    @Operation(
            summary = "Create new account",
            description = "Create a new account for the user if it doesn't exist"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP status 201 Account Created"
    )

    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest){

        return userService.createAccount(userRequest);
    }

    @PostMapping("/login")
    public BankResponse login(@RequestBody LoginDto loginDto){
        return userService.login(loginDto);
    }

    @Operation(
            summary = "Balance enquiry",
            description = "Used for checking the balance of the account "
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP status 200 Balanced fetched Successfully"
    )

    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.balanceEnquiry(enquiryRequest);
    }

    @GetMapping("nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.nameEnquiry(enquiryRequest);
    }

    @PostMapping("/credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return userService.creditDetails(creditDebitRequest);
    }

    @PostMapping("/debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return userService.debitAccount(creditDebitRequest);
    }

    @PostMapping("/transfer")
    public BankResponse transferToAccount(@RequestBody TransferRequest transferRequest){

        return userService.transferDetails(transferRequest);
    }





}
