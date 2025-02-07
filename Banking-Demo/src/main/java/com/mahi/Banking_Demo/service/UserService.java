package com.mahi.Banking_Demo.service;

import com.mahi.Banking_Demo.dto.BankResponse;
import com.mahi.Banking_Demo.dto.EnquiryRequest;
import com.mahi.Banking_Demo.dto.UserRequest;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
    String nameEnquiry(EnquiryRequest enquiryRequest);
}
