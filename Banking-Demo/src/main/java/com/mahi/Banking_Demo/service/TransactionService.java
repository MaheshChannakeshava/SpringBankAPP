package com.mahi.Banking_Demo.service;

import com.mahi.Banking_Demo.dto.TransactionRequest;
import com.mahi.Banking_Demo.entity.Transactions;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {
    void saveTransaction(TransactionRequest transactionRequest);
}
