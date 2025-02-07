package com.mahi.Banking_Demo.service;

import com.mahi.Banking_Demo.dto.AccountInfo;
import com.mahi.Banking_Demo.dto.BankResponse;
import com.mahi.Banking_Demo.dto.EmailDetails;
import com.mahi.Banking_Demo.dto.UserRequest;
import com.mahi.Banking_Demo.entity.Users;
import com.mahi.Banking_Demo.repository.UserRepository;
import com.mahi.Banking_Demo.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImp implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

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
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
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
}
