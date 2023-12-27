package com.paymybuddy.service;

import com.paymybuddy.model.Transaction;
import com.paymybuddy.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for the Transaction object.
 */
@Service
public class TransactionService {
    /**
     * Call the TransactionRepository.
     */
    private final TransactionRepository transactionsRepository;
    
    /**
     * TransactionService constructor.
     *
     * @param transactionsRepository to access at the table of transaction of the Database.
     */
    public TransactionService(TransactionRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }
    
    /**
     * Call the findAll method of the transaction repository.
     *
     * @return An iterable of all Transaction object present in the Database's transaction table.
     */
    public Iterable<Transaction> getAll() {
        return transactionsRepository.findAll();
    }
    
    /**
     * Call the findById method of the transaction repository.
     *
     * @param id id of the Transaction parsed.
     * @return The Transaction object with the id parsed.
     */
    public Optional<Transaction> getBy(Integer id) {
        return transactionsRepository.findById(id);
    }
    
    /**
     * Call the save method of the user repository.
     *
     * @param transaction the Transaction object parsed to save in the Database/s transaction table.
     * @return call save method of the transaction repository with the Transaction object parsed.
     */
    public Transaction save(Transaction transaction) {
        return transactionsRepository.save(transaction);
    }
    
    public void deleteBy(Integer id) {
        transactionsRepository.deleteById(id);
    }
}
