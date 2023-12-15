package com.paymybuddy.repository;

import com.paymybuddy.model.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryTest {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    private final Transaction TRANSACTION = new Transaction();
    
    @Test
    void transactionSaveTest(){
    transactionRepository.save(TRANSACTION);
        
    Assertions.assertEquals(TRANSACTION, entityManager.find(Transaction.class, TRANSACTION.getId()));
    }
    
    @Test
    void transactionUpdateTest(){
        entityManager.persist(TRANSACTION);
        String newConnexion = "newtest@gmail.com";
        TRANSACTION.setConnexion(newConnexion);
        
        transactionRepository.save(TRANSACTION);
        
        Assertions.assertEquals(newConnexion, entityManager.find(Transaction.class, TRANSACTION.getId()).getConnexion());
    }
    
    @Test
    void transactionFindByIdTest(){
        entityManager.persist(TRANSACTION);
        
        Optional<Transaction> findTransaction = transactionRepository.findById(TRANSACTION.getId());
        
        Assertions.assertEquals(TRANSACTION.getId(), findTransaction.get().getId());
    }
    
    @Test
    void transactionFindAllTest(){
        Transaction transaction = new Transaction();
        entityManager.persist(TRANSACTION);
        entityManager.persist(transaction);
        
        Iterable<Transaction> findTransactions = transactionRepository.findAll();
        
        assertThat(findTransactions).contains(TRANSACTION, transaction);
    }
    
    @Test
    void transactionDeleteByIdTest(){
        entityManager.persist(TRANSACTION);
        
        transactionRepository.deleteById(TRANSACTION.getId());
        
        Assertions.assertNull(entityManager.find(Transaction.class, TRANSACTION.getId()));
    }
}
