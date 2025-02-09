package com.mahi.Banking_Demo.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import com.mahi.Banking_Demo.dto.EmailDetails;
import com.mahi.Banking_Demo.entity.Transactions;
import com.mahi.Banking_Demo.entity.Users;
import com.mahi.Banking_Demo.repository.TransactionRepository;
import com.mahi.Banking_Demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Service
@AllArgsConstructor
@Slf4j
public class BankStatement {

    private TransactionRepository transactionRepository;
    private UserRepository userRepository;
    private EmailService emailService;
    private static final String File="M:\\statements\\MyStatements.pdf";

    public List<Transactions> generateStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException {

        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate,DateTimeFormatter.ISO_DATE);

        List<Transactions> transactionsList = transactionRepository.findAll().stream().filter(transactions -> transactions.getAccountNumber().equals(accountNumber))
                .filter(transactions -> transactions.getCreatedAt().equals(start))
                .filter(transactions -> transactions.getCreatedAt().equals(end)).toList();

        Users user = userRepository.findByAccountNumber(accountNumber);
        String customerName= user.getFirstName()+" "+user.getLastName()+" "+user.getOtherName();

        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        log.info("Setting Size of Document");
        OutputStream outputStream = new FileOutputStream(File);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("The National Bank"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.BLUE);
        bankName.setPadding(30f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("55, Bharath Nagar, T12 F5F6"));
        bankAddress.setBorder(0);
        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: "+start));
        customerInfo.setBorder(0);
        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statement.setBorder(0);
        PdfPCell lastDate = new PdfPCell(new Phrase("End Date: "+end));
        lastDate.setBorder(0);

        PdfPCell name = new PdfPCell(new Phrase("Customer Name: "+ customerName));
        name.setBorder(0);
        PdfPCell space= new PdfPCell();

        PdfPCell customerAddress = new PdfPCell(new Phrase("Customer Address: "+ user.getAddress()));
        customerAddress.setBorder(0);

        PdfPTable transactionTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("DATE"));
        date.setBorder(0);
        date.setBackgroundColor(BaseColor.BLUE);
        PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
        transactionType.setBorder(0);
        transactionType.setBackgroundColor(BaseColor.BLUE);
        PdfPCell amount= new PdfPCell(new Phrase("AMOUNT"));
        amount.setBorder(0);
        amount.setBackgroundColor(BaseColor.BLUE);
        PdfPCell status = new PdfPCell(new Phrase("STATUS"));
        status.setBorder(0);
        status.setBackgroundColor(BaseColor.BLUE);

        transactionTable.addCell(date);
        transactionTable.addCell(transactionType);
        transactionTable.addCell(amount);
        transactionTable.addCell(status);

        transactionsList.forEach(transactions -> {
            transactionTable.addCell(new Phrase(transactions.getCreatedAt().toString()));
            transactionTable.addCell(new Phrase(transactions.getTransactionType()));
            transactionTable.addCell(new Phrase(transactions.getAmount().toString()));
            transactionTable.addCell(new Phrase(transactions.getStatus()));
        });

        statementInfo.addCell(customerInfo);
        statementInfo.addCell(statement);
        statementInfo.addCell(lastDate);
        statementInfo.addCell(name);
        statementInfo.addCell(space);
        statementInfo.addCell(customerAddress);

        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionTable);

        EmailDetails emailDetails= EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("STATEMENT OF ACCOUNT")
                .mailBody("Kindly Find the Attached Bank Statement")
                .attachment(File)
                .build();

        emailService.sendEmailWithAttachments(emailDetails);


        return transactionsList;
    }
    
//    private void designStatement(List<Transactions> transactionsList) throws FileNotFoundException, DocumentException {
//
//    }
}
