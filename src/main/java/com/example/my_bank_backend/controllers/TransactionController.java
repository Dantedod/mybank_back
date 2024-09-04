package com.example.my_bank_backend.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.my_bank_backend.domain.account.Account;
import com.example.my_bank_backend.domain.transaction.Transaction;
import com.example.my_bank_backend.dto.TransactionRequestDto;
import com.example.my_bank_backend.dto.TransactionResponseDto;
import com.example.my_bank_backend.repositories.AccountRepository;
import com.example.my_bank_backend.repositories.TransactionRepository;
import com.example.my_bank_backend.service.TransactionService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private TransactionService transactionService;
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionController(TransactionService transactionService, AccountRepository accountRepository,
            TransactionRepository transactionRepository) {
        this.transactionService = transactionService;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<TransactionResponseDto> createTransaction(
            @RequestBody TransactionRequestDto transactionRequestDto) {
        try {
            TransactionResponseDto response = transactionService.processTransaction(
                    transactionRequestDto.cpfSender(),
                    transactionRequestDto.cpfReceiver(),
                    transactionRequestDto.amount(),
                    transactionRequestDto.paymentDescription(),
                    transactionRequestDto.transactionType());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<List<TransactionResponseDto>> getAllTransactionsByCpf(@PathVariable String cpf) {

        Optional<Account> optAccount = accountRepository.findByCpf(cpf);

        if (optAccount.isPresent()) {
            Account account = optAccount.get();

            List<Transaction> transactions = transactionRepository.findBySenderAccountIdOrReceiverAccountId(account, account);

            if (transactions.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                List<TransactionResponseDto> responseDtos = transactions.stream()
                        .map(tx -> new TransactionResponseDto(
                                tx.getId(),
                                tx.getSenderAccountCpf(),
                                tx.getSenderAccountId().getUser().getName(),
                                tx.getReceiverAccountCpf(),
                                tx.getReceiverAccountId().getUser().getName(),
                                tx.getAmount(),
                                tx.getPaymentDescription(),
                                tx.getTransactionDate(),
                                tx.getTransactionType()
                        ))
                        .collect(Collectors.toList());
                return ResponseEntity.ok(responseDtos);
            }
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Transaction> getTransactionByCpf(@PathVariable Long id) {

        Optional<Transaction> transaction = transactionRepository.findById(id);

        if (transaction.isPresent()) {
            return ResponseEntity.ok(transaction.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
