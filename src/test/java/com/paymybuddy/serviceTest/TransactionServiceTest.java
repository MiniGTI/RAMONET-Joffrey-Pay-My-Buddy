package com.paymybuddy.serviceTest;

import com.paymybuddy.model.Transaction;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Test class for the TransactionService.
 */
@SpringBootTest
@ActiveProfiles("test")
public class TransactionServiceTest {
    /**
     * TransactionService call.
     */
    @Autowired
    private TransactionService transactionService;
    /**
     * TransactionRepository mock.
     */
    @MockBean
    private TransactionRepository transactionRepository;
    /**
     * A simple BankAccount object initialization for test.
     */
    private final Transaction TRANSACTION =
            new Transaction("A simple transaction test", "test2@gmail.com", new BigDecimal(50));
    /**
     * Test of the save() method of TransactionService.
     */
    @Test
    void transactionServiceSaveTest() {
        when(transactionRepository.save(TRANSACTION)).thenReturn(TRANSACTION);
        
        Transaction result = transactionService.save(TRANSACTION);
        
        Assertions.assertEquals(TRANSACTION, result);
    }
    /**
     * Test of the GetBy() method of TransactionService.
     */
    @Test
    void transactionServiceGetByIdTest() {
        when(transactionRepository.findById(1)).thenReturn(Optional.ofNullable(TRANSACTION));
        
        Optional<Transaction> result = transactionService.getBy(1);
        
        Assertions.assertEquals(TRANSACTION.getTransactionNumber(), result.get()
                .getTransactionNumber());
    }
    /**
     * Test of the GetAll() method of TransactionService.
     */
    @Test
    void transactionServiceGetAllTest() {
        List<Transaction> Transactions = new ArrayList<>();
        Transactions.add(TRANSACTION);
        
        when(transactionRepository.findAll()).thenReturn(Transactions);
        
        Iterable<Transaction> result = transactionService.getAll();
        
        int count = 0;
        
        for(Transaction t : result) {
            count++;
        }
        
        Assertions.assertEquals(Transactions.size(), count);
    }
    /**
     * Test of the DeleteBy() method of TransactionService.
     */
    @Test
    void transactionServiceDeleteByTest() {
        
        doNothing().when(transactionRepository)
                .deleteById(1);
        transactionService.deleteBy(1);
        
        Assertions.assertDoesNotThrow(() -> transactionService.deleteBy(1));
    }
}
