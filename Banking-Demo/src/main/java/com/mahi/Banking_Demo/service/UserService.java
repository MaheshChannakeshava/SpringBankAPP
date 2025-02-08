package com.mahi.Banking_Demo.service;

import com.mahi.Banking_Demo.dto.*;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
    String nameEnquiry(EnquiryRequest enquiryRequest);

    BankResponse creditDetails(CreditDebitRequest creditDebitRequest);
    BankResponse debitAccount(CreditDebitRequest creditDebitRequest);
    BankResponse transferDetails(TransferRequest transferRequest);
}
