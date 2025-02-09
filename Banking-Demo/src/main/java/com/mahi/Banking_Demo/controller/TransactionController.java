package com.mahi.Banking_Demo.controller;

import com.itextpdf.text.DocumentException;
import com.mahi.Banking_Demo.entity.Transactions;
import com.mahi.Banking_Demo.service.BankStatement;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/bankStatement")
@AllArgsConstructor
public class TransactionController {

   // @Autowired
   private BankStatement bankStatement;

    @GetMapping()
    public List<Transactions> generateStatement(@RequestParam String accountNumber,
                                                    @RequestParam String startDate,
                                                    @RequestParam String endDate) throws DocumentException, FileNotFoundException {
        return bankStatement.generateStatement(accountNumber, startDate, endDate);
    }
}
