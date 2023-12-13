package com.paymybuddy.repository;


import com.paymybuddy.model.BankAccount;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

/**
 * Test class for the BankAccountRepository.
 */
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BankAccountRepositoryTest {
    /**
     * BankAccountRepository call.
     */
    @Autowired
    private BankAccountRepository bankAccountRepository;

    /**
     * A simple BankAccount object initialization for test.
     */
    private final BankAccount BANKACCOUNT = new BankAccount();
    /**
    @AfterAll
    static void cleanDB(){
        dataBaseCleanUp.cleanDB();
    }
    */
    /**
     * Test of the save() method of BankAccountRepository.
     */
    @Test
    @Order(1)
    void saveBankAccountRepositoryTest() {
        BankAccount bankAccountsaved = bankAccountRepository.save(BANKACCOUNT);
  
        Assertions.assertEquals(bankAccountsaved, BANKACCOUNT);
    }
    
    /**
     * Test of the findBy() method of BankAccountRepository.
     */
    @Test
    @Order(2)
    void findByIdBankAccountRepositoryTest() {
        Optional<BankAccount> bankaccountFound = bankAccountRepository.findById(1);
        
        Assertions.assertEquals("7d90428b-d6f2-4f9e-8587-7dd2d8bef932", bankaccountFound.get()
                .getIban());
    }
    
    /**
     * Test of the findAll() method of BankAccountRepository.
     */
    @Test
    @Order(3)
    void findAllBankAccountRepositoryTest() {
        Iterable<BankAccount> bankAccounts = bankAccountRepository.findAll();
        
        int counter = 0;
        for(BankAccount b : bankAccounts) {
            counter++;
        }
        
        Assertions.assertEquals(3, counter);
    }
    
    /**
     * Test of the delete() method of BankAccountRepository.
     */
    @Test
    @Order(4)
    void deleteByIdBankAccountRepositoryTest() {
        bankAccountRepository.deleteById(3);
        
        Iterable<BankAccount> bankAccounts = bankAccountRepository.findAll();
        
        int counter = 0;
        for(BankAccount b : bankAccounts) {
            counter++;
        }
        
        Assertions.assertEquals(2, counter);
    }
}
