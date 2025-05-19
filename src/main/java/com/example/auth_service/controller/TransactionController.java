package com.example.auth_service.controller;

import com.example.auth_service.dto.TxnRequestDTO;
import com.example.auth_service.dto.TxnResponseDTO;
import com.example.auth_service.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/{userId}/transactions")
    public ResponseEntity<?> addTransaction(@PathVariable Long userId, @RequestBody TxnRequestDTO txnRequestDTO) {
        Object response = transactionService.addTxnToUser(userId, txnRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions/{txnId}")
    public ResponseEntity<Object> getTransactionById(@PathVariable Long txnId) {
        Optional<TxnResponseDTO> optionalTxn = transactionService.getTransactionFromId(txnId);

        return optionalTxn.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No transaction found with ID: " + txnId));
    }

}
