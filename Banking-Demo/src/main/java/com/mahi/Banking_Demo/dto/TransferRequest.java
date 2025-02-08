package com.mahi.Banking_Demo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferRequest {

    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private double amount;
}
