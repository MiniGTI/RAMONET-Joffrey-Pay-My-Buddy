package com.paymybuddy.dto;

import com.paymybuddy.model.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Object to get data from the form of the transfer page.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    
    private String connection;
    private BigDecimal amount;
    private String description;
    private BankAccount bankAccount;
}
