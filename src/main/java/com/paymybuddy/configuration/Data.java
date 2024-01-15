package com.paymybuddy.configuration;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.repository.BankAccountRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Class to create the feeAccount if isn't exist.
 */
@Component
public class Data {
    
    /**
     * Call the BankAccountRepository to perform CRUDs request to the database.
     */
    BankAccountRepository bankAccountRepository;
    
    /**
     * The class constructor.
     *
     * @param bankAccountRepository to perform the sav method.
     */
    public Data(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }
    
    /**
     * Method to check if the feeAccount exist in the bank_account table, and if doesn't exist insert it.
     * The feeAccount had always the first id.
     * To preserve security, if the feeAccount is removed from the database for any reason, it's recreated with a new random IBAN and SWIFT.
     *
     * @param event
     */
    @EventListener
    public void feeAccountLoader(ApplicationReadyEvent event) {
        if(
        bankAccountRepository.findById(1).isEmpty()) {
            bankAccountRepository.save(new BankAccount(1, new BigDecimal("0.00"), UUID.randomUUID()
                    .toString(), UUID.randomUUID()
                    .toString(), null));
        }
    }
}
