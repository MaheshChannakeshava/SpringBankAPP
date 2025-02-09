package com.mahi.Banking_Demo.service;

import com.mahi.Banking_Demo.config.JwtTokenProvider;
import com.mahi.Banking_Demo.dto.*;
import com.mahi.Banking_Demo.entity.Role;
import com.mahi.Banking_Demo.entity.Users;
import com.mahi.Banking_Demo.repository.UserRepository;
import com.mahi.Banking_Demo.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.image.BandCombineOp;
import java.math.BigDecimal;

@Service
public class UserServiceImp implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        /** This method is used to save new user to the DB
        * Check if the user already exist are not
        * */

        if(userRepository.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        Users newUser = Users.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .role(Role.valueOf("ROLE_ADMIN"))
                .build();

        Users savedUser = userRepository.save(newUser);

        //to send email alerts to customer
        EmailDetails emailDetails= EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account Created")
                .mailBody("Congratulations on the creation of your Account with us!!.\nYour Account details are" +
                        "Account Name: "+savedUser.getFirstName()+" "+savedUser.getLastName()+" "+savedUser.getOtherName()+ "\nAccountNumber: "+savedUser.getAccountNumber())
                .build();
        emailService.sendEmailALerts(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName()+" "+savedUser.getLastName()+" "+savedUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public BankResponse login(LoginDto loginDto){
        Authentication authentication = null;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        EmailDetails loginAlerts= EmailDetails.builder()
                .subject("You are logged in!!")
                .recipient(loginDto.getEmail())
                .mailBody("You have logged in to your account. If you have npt initiated it please contact the bank")
                .build();
        emailService.sendEmailALerts(loginAlerts);

        return BankResponse.builder()
                .responseCode("Login Success")
                .responseMessage(jwtTokenProvider.generateToken(authentication))
               // .accountInfo()
                .build();

    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        //First lets check if we have the requested accountNumber

        boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();

        }
        Users foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(foundUser.getAccountNumber())
                        .accountName(foundUser.getFirstName()+" "+foundUser.getLastName()+" "+foundUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExist){
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;

        }

        Users foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUser.getFirstName()+" "+foundUser.getLastName()+" "+foundUser.getOtherName();


    }

    @Override
    public BankResponse creditDetails(CreditDebitRequest creditDebitRequest) {
        //Check for account availability
        boolean isAccountExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if(!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();

        }

        Users userToCredit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditDebitRequest.getAmount()));
        userRepository.save(userToCredit);
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .amount(creditDebitRequest.getAmount())
                .transactionType("CREDIT")
                .build();

        transactionService.saveTransaction(transactionRequest);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDIT_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREDIT_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(userToCredit.getAccountNumber())
                        .accountName(userToCredit.getFirstName()+" "+userToCredit.getLastName()+" "+userToCredit.getOtherName())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
        /* In this we need to check if we have the account and
        if the account as  more money than the withdraw amount
         */

        boolean isAccountExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if(!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();

        }

        Users userToDebit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        if(userToDebit.getAccountBalance().compareTo(creditDebitRequest.getAmount())<0){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_LOW_BALANCE)
                    .responseMessage(AccountUtils.ACCOUNT_LOW_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();

        }

        else{
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(creditDebitRequest.getAmount()));
            userRepository.save(userToDebit);
            TransactionRequest transactionRequestDebit = TransactionRequest.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .amount(creditDebitRequest.getAmount())
                    .transactionType("DEBIT")
                    .build();

            transactionService.saveTransaction(transactionRequestDebit);

            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBIT_SUCCESS)
                    .responseMessage(AccountUtils.ACCOUNT_DEBIT_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountBalance(userToDebit.getAccountBalance())
                            .accountNumber(userToDebit.getAccountNumber())
                            .accountName(userToDebit.getFirstName()+" "+userToDebit.getLastName()+" "+userToDebit.getOtherName())
                            .build())
                    .build();
        }

       // return null;
    }

    @Override
    public BankResponse transferDetails(TransferRequest transferRequest) {
       // boolean isSourceAccExist = userRepository.existsByAccountNumber(transferRequest.getSourceAccountNumber());
        boolean isDestinationAccExist = userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber());
        if(!isDestinationAccExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();

        }
//        if(!isSourceAccExist){
//            return BankResponse.builder()
//                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST)
//                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
//                    .accountInfo(null)
//                    .build();
//
//        }

        Users sourceAccount = userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
        if(sourceAccount.getAccountBalance().compareTo(BigDecimal.valueOf(transferRequest.getAmount()))<0){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_LOW_BALANCE)
                    .responseMessage(AccountUtils.ACCOUNT_LOW_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();

        }

        else{
            sourceAccount.setAccountBalance(sourceAccount.getAccountBalance().subtract(BigDecimal.valueOf(transferRequest.getAmount())));
            userRepository.save(sourceAccount);
            EmailDetails debitAlert = EmailDetails.builder()
                    .subject("Debit Alert")
                    .recipient(sourceAccount.getEmail())
                    .mailBody("A total amount of "+ transferRequest.getAmount() + " has been debited from your account")
                    .build();

            emailService.sendEmailALerts(debitAlert);
            TransactionRequest transactionRequestDebitTransfer = TransactionRequest.builder()
                    .accountNumber(sourceAccount.getAccountNumber())
                    .amount(BigDecimal.valueOf(transferRequest.getAmount()))
                    .transactionType("DEBIT")
                    .build();

            transactionService.saveTransaction(transactionRequestDebitTransfer);

            Users destinationAccount = userRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());
            destinationAccount.setAccountBalance(destinationAccount.getAccountBalance().add(BigDecimal.valueOf(transferRequest.getAmount())));
            userRepository.save(destinationAccount);

            EmailDetails creditAlert = EmailDetails.builder()
                    .subject("Credit Alert")
                    .recipient(destinationAccount.getEmail())
                    .mailBody("A total amount of "+ transferRequest.getAmount()+ " has been credited to your account")
                    .build();

            emailService.sendEmailALerts(creditAlert);
            TransactionRequest transactionRequestCreditTransfer = TransactionRequest.builder()
                    .accountNumber(destinationAccount.getAccountNumber())
                    .amount(BigDecimal.valueOf(transferRequest.getAmount()))
                    .transactionType("CREDIT")
                    .build();

            transactionService.saveTransaction(transactionRequestCreditTransfer);


            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBIT_SUCCESS)
                    .responseMessage(AccountUtils.ACCOUNT_DEBIT_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountBalance(destinationAccount.getAccountBalance())
                            .accountNumber(destinationAccount.getAccountNumber())
                            .accountName(destinationAccount.getFirstName()+" "+destinationAccount.getLastName()+" "+destinationAccount.getOtherName())
                            .build())
                    .build();
        }

    }
}
