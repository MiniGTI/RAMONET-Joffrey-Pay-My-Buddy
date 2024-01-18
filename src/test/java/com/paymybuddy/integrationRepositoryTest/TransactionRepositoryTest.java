package com.paymybuddy.integrationRepositoryTest;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryTest {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    private final BankAccount BANKACCOUNT =
            new BankAccount();
    private final BankAccount BANKACCOUNT2 = new BankAccount();
    private final Transaction TRANSACTION = new Transaction("First transaction", "user2test@email.com", new BigDecimal("150.00"),
            BANKACCOUNT);
    
    
    @BeforeEach
    public void setUp(){
        entityManager.persist(BANKACCOUNT);
    }
    
    @Test
    void transactionSaveTest(){
        transactionRepository.save(TRANSACTION);
        
        Assertions.assertEquals(TRANSACTION, entityManager.find(Transaction.class, TRANSACTION.getId()));
    }
    
    @Test
    void transactionUpdateTest(){
        entityManager.persist(TRANSACTION);
        String newConnexion = "user2test@gmail.com";
        TRANSACTION.setConnection(newConnexion);
        
        transactionRepository.save(TRANSACTION);
        
        Assertions.assertEquals(newConnexion, entityManager.find(Transaction.class, TRANSACTION.getId()).getConnection());
    }
    
    @Test
    void transactionFindByIdTest(){
        entityManager.persist(TRANSACTION);
        
        Optional<Transaction> findTransaction = transactionRepository.findById(TRANSACTION.getId());
        
        Assertions.assertEquals(TRANSACTION.getId(), findTransaction.get().getId());
    }
    
    @Test
    void transactionFindAllTest(){
        entityManager.persist(BANKACCOUNT2);
        Transaction transaction = new Transaction("Second transaction", "user1test@email.com", new BigDecimal("100.00"),
                BANKACCOUNT2);
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
