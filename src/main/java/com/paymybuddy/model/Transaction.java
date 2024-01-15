package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Transaction model.
 * transactions table in the database.
 * Every money transfer between two Users generate a transaction object.
 * transaction number is a random UUID generated at the transaction creation.
 * connection is the User who while be credited.
 * amount is the amount of the money movement.
 * description is a note for the user who send money.
 * createdAt is the local date time of the transaction creation.
 * ----------
 * They're a ManyToOne relation with bankAccount with the attribute bank_account_id.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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
