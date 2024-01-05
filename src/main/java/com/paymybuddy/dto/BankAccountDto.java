package com.paymybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Object to get the amount value from the transfer form.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDto {

private BigDecimal amount;
}
