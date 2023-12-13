package com.paymybuddy.service;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankAccountService {
    
    private final BankAccountRepository bankAccountRepository;
    
    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }
    
    public Iterable<BankAccount> getAll() {
        return bankAccountRepository.findAll();
    }
    
    public Optional<BankAccount> getBy(int id) {
        return bankAccountRepository.findById(id);
    }
    
    public BankAccount save(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }
    
    public void deleteBy(int id) {
        bankAccountRepository.deleteById(id);
    }
}
