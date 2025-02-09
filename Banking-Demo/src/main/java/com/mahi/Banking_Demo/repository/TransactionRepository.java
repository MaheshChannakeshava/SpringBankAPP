package com.mahi.Banking_Demo.repository;

import com.mahi.Banking_Demo.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, String> {
}
