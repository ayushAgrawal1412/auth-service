package com.example.auth_service.repository;

import com.example.auth_service.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TxnRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findById(Long id);
}
