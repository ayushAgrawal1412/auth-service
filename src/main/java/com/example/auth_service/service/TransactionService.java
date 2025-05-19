package com.example.auth_service.service;

import com.example.auth_service.dto.TxnRequestDTO;
import com.example.auth_service.dto.TxnResponseDTO;
import com.example.auth_service.model.Transaction;
import com.example.auth_service.model.User;
import com.example.auth_service.repository.TxnRepository;
import com.example.auth_service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionService {

    private final TxnRepository txnRepository;
    private final UserRepository userRepository;

    TransactionService(TxnRepository txnRepository, UserRepository userRepository) {
        this.txnRepository = txnRepository;
        this.userRepository = userRepository;
    }

    public Object addTxnToUser(Long userId, TxnRequestDTO requestDTO) {

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return "User not found with ID: " + userId;
        }

        // Preparing the txn object
        User user = optionalUser.get();
        Transaction txn = new Transaction();
        txn.setName(requestDTO.getName());
        txn.setAmount(requestDTO.getAmount());
        txn.setUser(user);

        // saving txn to db
        Transaction savedTxn = txnRepository.save(txn);

        return convertToTxnResponseDTO(savedTxn);
    }

    public Optional<TxnResponseDTO> getTransactionFromId(Long txnId) {
        return txnRepository.findById(txnId)
                .map(this::convertToTxnResponseDTO);
    }

    private TxnResponseDTO convertToTxnResponseDTO(Transaction transaction) {
        TxnResponseDTO responseDTO = new TxnResponseDTO();
        responseDTO.setName(transaction.getName());
        responseDTO.setAmount(transaction.getAmount());
        responseDTO.setUserId(transaction.getUser().getId());
        return responseDTO;
    }
}
