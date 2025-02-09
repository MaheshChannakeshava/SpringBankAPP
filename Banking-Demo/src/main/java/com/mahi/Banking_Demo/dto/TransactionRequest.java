package com.mahi.Banking_Demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionRequest {
    private String transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private String status;
}
