package com.paymybuddy.serviceTest;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.repository.BankAccountRepository;
import com.paymybuddy.service.BankAccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Test class for the BankAccountService.
 */
@SpringBootTest
@ActiveProfiles("test")
public class BankAccountServiceTest {
    /**
     * BankAccountService call.
     */
    @Autowired
    private BankAccountService bankAccountService;
    /**
     * BankAccountRepository mock.
     */
    @MockBean
    private BankAccountRepository bankAccountRepository;
    /**
     * A simple BankAccount object initialization for test.
     */
    private final BankAccount BANKACCOUNT = new BankAccount();
    /**
     * Test of the save() method of BankAccountService.
     */
    @Test
    void bankAccountServiceSaveTest() {
        when(bankAccountRepository.save(BANKACCOUNT)).thenReturn(BANKACCOUNT);
        
        BankAccount result = bankAccountService.save(BANKACCOUNT);
        
        Assertions.assertEquals(BANKACCOUNT, result);
    }
    /**
     * Test of the GetBy() method of BankAccountService.
     */
    @Test
    void bankAccountServiceGetByIdTest() {
        when(bankAccountRepository.findById(1)).thenReturn(Optional.ofNullable(BANKACCOUNT));
        
        Optional<BankAccount> result = bankAccountService.getBy(1);
        
        Assertions.assertEquals(BANKACCOUNT.getIban(), result.get()
                .getIban());
    }
    /**
     * Test of the GetAll() method of BankAccountService.
     */
    @Test
    void bankAccountServiceGetAllTest() {
        List<BankAccount> bankAccounts = new ArrayList<>();
        bankAccounts.add(BANKACCOUNT);
        
        when(bankAccountRepository.findAll()).thenReturn(bankAccounts);
        
        Iterable<BankAccount> result = bankAccountService.getAll();
        
        int count = 0;
        
        for(BankAccount b : result) {
            count++;
        }
        
        Assertions.assertEquals(bankAccounts.size(), count);
    }
    /**
     * Test of the DeleteBy() method of BankAccountService.
     */
    @Test
    void bankAccountServiceDeleteByTest() {
        
        doNothing().when(bankAccountRepository)
                .deleteById(1);
        bankAccountService.deleteBy(1);
        
        Assertions.assertDoesNotThrow(() -> bankAccountService.deleteBy(1));
    }
}
