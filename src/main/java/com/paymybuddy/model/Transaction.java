package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "transaction_number", length = 36)
    private String transactionNumber;
    @Column(length = 40)
    private String connection;
    @Column(length = 7)
    private BigDecimal amount;
    private String description;
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
