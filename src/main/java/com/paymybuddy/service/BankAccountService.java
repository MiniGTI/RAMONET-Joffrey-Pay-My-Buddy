package com.paymybuddy.service;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for the BankAccount object.
 */
@Service
public class BankAccountService {
    
    /**
     * Call the BankAccountRepository.
     */
    private final BankAccountRepository bankAccountRepository;
    
    /**
     * BankAccountService constructor.
     *
     * @param bankAccountRepository to access at the table of BankAccount of the Database.
     */
    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }
    
    /**
     * Call the findAll method of the bankAccount repository.
     *
     * @return An iterable of all BankAccount object present in the Database's bankAccount table.
     */
    public Iterable<BankAccount> getAll() {
        return bankAccountRepository.findAll();
    }
    
    /**
     * Call the findById method of the bankAccount repository.
     *
     * @param id id of the User parsed.
     * @return The BankAccount object with the id parsed.
     */
    public Optional<BankAccount> getBy(int id) {
        return bankAccountRepository.findById(id);
    }
    
    /**
     * Call the save method of the bankAccount repository.
     *
     * @param bankAccount the new BankAccount object parsed to save.
     * @return call save method of the bankAccount repository.
     */
    public BankAccount save(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }
    
    /**
     * Call the deleteBy method of the bankAccount repository.
     *
     * @param id id of the BankAccount parsed.
     */
    public void deleteBy(int id) {
        bankAccountRepository.deleteById(id);
    }
}
