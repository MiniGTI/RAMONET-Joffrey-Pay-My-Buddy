package com.paymybuddy.integrationRepositoryTest;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.repository.BankAccountRepository;
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
public class BankAccountRepositoryTest {
    
    @Autowired
    private BankAccountRepository bankAccountRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    private final BankAccount BANKACCOUNT = new BankAccount();
    
    @Test
    void bankAccountSaveTest(){
        BankAccount savedBankAccount = bankAccountRepository.save(BANKACCOUNT);
        
        Assertions.assertEquals(BANKACCOUNT, entityManager.find(BankAccount.class, savedBankAccount.getId()) );
    }
    
    @Test
    void bankAccountFindByIdTest(){
        entityManager.persist(BANKACCOUNT);
        
        Optional<BankAccount> findBankAccount = bankAccountRepository.findById(BANKACCOUNT.getId());
        
        Assertions.assertEquals(BANKACCOUNT.getId(), findBankAccount.get().getId());
    }
    
    @Test
    void bankAccountFindAllTest(){
        BankAccount bankAccount = new BankAccount();
        entityManager.persist(BANKACCOUNT);
        entityManager.persist(bankAccount);
        
        Iterable<BankAccount> findBankAccounts = bankAccountRepository.findAll();
        
        assertThat(findBankAccounts).contains(BANKACCOUNT, bankAccount);
    }
    
    @Test
    void bankAccountDeleteByIdTest(){
        entityManager.persist(BANKACCOUNT);
        
        bankAccountRepository.deleteById(BANKACCOUNT.getId());
        
        Assertions.assertNull(entityManager.find(BankAccount.class, BANKACCOUNT.getId()));
    }
    
}
