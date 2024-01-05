package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Transaction model.
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "transaction_number")
    private String transactionNumber;
    private String connection;
    private BigDecimal amount;
    private String description;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @ManyToOne(
            cascade = CascadeType.ALL)
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;
    
    public Transaction(String description, String connection, BigDecimal amount, BankAccount bankAccount) {
        this.transactionNumber = UUID.randomUUID()
                .toString();
        this.createdAt = LocalDateTime.now();
        this.description = description;
        this.connection = connection;
        this.amount = amount;
        this.bankAccount = bankAccount;
    }
}
