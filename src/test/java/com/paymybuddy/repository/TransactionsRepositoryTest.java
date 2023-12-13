package com.paymybuddy.repository;

import com.paymybuddy.model.Transaction;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Test class for the TransactionsRepository.
 */
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransactionsRepositoryTest {
    
    /**
     * TransactionsRepository call.
     */
    @Autowired
    private TransactionRepository transactionsRepository;
    
    /**
     * A simple Transaction object initialization for test.
     */
    private final Transaction TRANSACTION =
            new Transaction("A simple transaction test", "test2@gmail.com", new BigDecimal(50));
    
    /**
     * Test of the save() method of TransactionsRepository.
     */
    @Test
    @Order(1)
    void saveTransactionRepositoryTest() {
        Transaction transactionSaved = transactionsRepository.save(TRANSACTION);
        
        Assertions.assertEquals(TRANSACTION.getConnexion(), transactionSaved.getConnexion());
    }
    
    /**
     * Test of the findBy() method of TransactionsRepository.
     */
    @Test
    @Order(2)
    void findByIdTransactionRepositoryTest() {
        Optional<Transaction> transactionFound = transactionsRepository.findById(1);
        
        Assertions.assertEquals("test2@gmail.com", transactionFound.get()
                .getConnexion());
    }
    
    /**
     * Test of the findAll() method of TransactionsRepository.
     */
    @Test
    @Order(3)
    void findAllTransactionRepositoryTest() {
        Iterable<Transaction> transactions = transactionsRepository.findAll();
        
        int counter = 0;
        for(Transaction t : transactions) {
            counter++;
        }
        
        Assertions.assertEquals(3, counter);
    }
    
    /**
     * Test of the delete() method of TransactionsRepository.
     */
    @Test
    @Order(4)
    void deleteByEmailTransactionRepositoryTest() {
        transactionsRepository.deleteById(3);
        
        Iterable<Transaction> transactions = transactionsRepository.findAll();
        
        int counter = 0;
        for(Transaction t : transactions) {
            counter++;
        }
        
        Assertions.assertEquals(2, counter);
    }
}
