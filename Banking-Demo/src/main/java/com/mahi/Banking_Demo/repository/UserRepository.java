package com.mahi.Banking_Demo.repository;

import com.mahi.Banking_Demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Boolean existsByEmail(String email);

    Optional<Users> findByEmail(String email);

    Boolean existsByAccountNumber(String accountNumber);

    Users findByAccountNumber(String accountNumber);
}
