package com.mahi.Banking_Demo.service;

import com.mahi.Banking_Demo.dto.TransactionRequest;
import com.mahi.Banking_Demo.entity.Transactions;
import com.mahi.Banking_Demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImp implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionRequest transactionRequest) {

        Transactions transactions = Transactions.builder()
                .transactionType(transactionRequest.getTransactionType())
                .accountNumber(transactionRequest.getAccountNumber())
                .amount(transactionRequest.getAmount())
                .status("SUCCESS")
                .build();

        transactionRepository.save(transactions);
        System.out.println("The transaction is saved Successfully");



    }
}
