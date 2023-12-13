package com.paymybuddy.service;

import com.paymybuddy.model.Transaction;
import com.paymybuddy.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionsRepository;
    
    public TransactionService(TransactionRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }
    
    public Iterable<Transaction> getAll(){
        return transactionsRepository.findAll();
    }
    
    public Optional<Transaction> getBy(Integer id){
        return transactionsRepository.findById(id);
    }
    
    public Transaction save(Transaction transactions) {
        return transactionsRepository.save(transactions);
    }
    
    public void deleteBy(Integer id) {
        transactionsRepository.deleteById(id);
    }
}
