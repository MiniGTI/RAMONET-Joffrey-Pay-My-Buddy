package com.paymybuddy.dto;

import com.paymybuddy.model.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Object to get the data input from the transaction form.
 * Used to parse data to perform and create a new transaction.
 * Used to parse data to update BankAccount's balance attribute.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    
    private String connection;
    private BigDecimal amount;
    private String description;
    private BankAccount bankAccount;
    
}
