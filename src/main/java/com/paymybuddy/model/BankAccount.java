package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "bank_account")
public class BankAccount {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private BigDecimal balance;
    private String iban;
    private String swift;
    
    @OneToMany (
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "bank_account_id")
    private List<Transaction> transactions;
    public BankAccount() {
        this.balance = new BigDecimal("0.00");
        this.iban = UUID.randomUUID().toString();
        this.swift = UUID.randomUUID().toString();
    }
}
