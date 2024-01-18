package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * BankAccount entity.
 * bank_account table in the database
 * Used to store a balance with two internationals bank account identifier.
 * It's generate at the user registration.
 * iban and swift are two random UUID, generated at the bankAccount creation.
 * ----------
 * They're a OneToMany relation with the transactions table, linked by the bank_account_id column.
 */
@Entity
@Data
@AllArgsConstructor
@Table(name = "bank_account")
public class BankAccount {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 7, nullable = false)
    private BigDecimal balance;
    @Column(length = 36, nullable = false)
    private String iban;
    @Column(length = 36, nullable = false)
    private String swift;
    
    @OneToMany(
            mappedBy = "bankAccount",
            cascade = CascadeType.ALL)
    private List<Transaction> transactions;
    
    public BankAccount() {
        this.balance = new BigDecimal("0.00");
        this.iban = UUID.randomUUID()
                .toString();
        this.swift = UUID.randomUUID()
                .toString();
    }
}
