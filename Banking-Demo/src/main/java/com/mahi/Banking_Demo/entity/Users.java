package com.mahi.Banking_Demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String otherName;
    private String address;
    private String stateOfOrigin;
    private String accountNumber;
    private BigDecimal accountBalance;
    private String email;
    private String phoneNumber;
    private String alternativePhoneNumber;
    private String status;

    @CreationTimestamp
    private LocalTime createdAt;
    @UpdateTimestamp
    private LocalTime modifiedAt;

//    public Users() {
//    }
//
//    public Users(Long id, String firstName, String lastName, String otherName, String address, String stateOfOrigin, String accountNumber, BigDecimal accountBalance, String email, String phoneNumber, String alternativePhoneNumber, String status, LocalTime createdAt, LocalTime modifiedAt) {
//        this.id = id;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.otherName = otherName;
//        this.address = address;
//        this.stateOfOrigin = stateOfOrigin;
//        this.accountNumber = accountNumber;
//        this.accountBalance = accountBalance;
//        this.email = email;
//        this.phoneNumber = phoneNumber;
//        this.alternativePhoneNumber = alternativePhoneNumber;
//        this.status = status;
//        this.createdAt = createdAt;
//        this.modifiedAt = modifiedAt;
//    }
//
//
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getOtherName() {
//        return otherName;
//    }
//
//    public void setOtherName(String otherName) {
//        this.otherName = otherName;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public String getStateOfOrigin() {
//        return stateOfOrigin;
//    }
//
//    public void setStateOfOrigin(String stateOfOrigin) {
//        this.stateOfOrigin = stateOfOrigin;
//    }
//
//    public String getAccountNumber() {
//        return accountNumber;
//    }
//
//    public void setAccountNumber(String accountNumber) {
//        this.accountNumber = accountNumber;
//    }
//
//    public BigDecimal getAccountBalance() {
//        return accountBalance;
//    }
//
//    public void setAccountBalance(BigDecimal accountBalance) {
//        this.accountBalance = accountBalance;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getAlternativePhoneNumber() {
//        return alternativePhoneNumber;
//    }
//
//    public void setAlternativePhoneNumber(String alternativePhoneNumber) {
//        this.alternativePhoneNumber = alternativePhoneNumber;
//    }
//
//    public LocalTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(LocalTime createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public LocalTime getModifiedAt() {
//        return modifiedAt;
//    }
//
//    public void setModifiedAt(LocalTime modifiedAt) {
//        this.modifiedAt = modifiedAt;
//    }
}
