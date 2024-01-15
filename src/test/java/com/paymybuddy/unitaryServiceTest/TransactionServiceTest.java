package com.paymybuddy.unitaryServiceTest;

import com.paymybuddy.dto.TransactionDto;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.service.TransactionService;
import com.paymybuddy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransactionServiceTest {
    
    @Autowired
    private TransactionService transactionService;
    
    @MockBean
    private TransactionRepository transactionRepository;
    
    @MockBean
    private UserService userService;
    
    private final BankAccount BANKACCOUNT =
            new BankAccount(1, new BigDecimal("2000.00"), "67acb3ed-6d46-4a15-ad02-d0d0be604fc1",
                    "6e3429a6-f593-45dc-8a93-1c9c3c4f32da", new ArrayList<>());
    private final BankAccount BANKACCOUNT2 =
            new BankAccount(2, new BigDecimal("1000.00"), "301dcceb-49d9-47da-92cc-c386f88dfe4a",
                    "fc3e7eb7-4390-465d-8c54-2fcc65315d7a", new ArrayList<>());
    private final User
            USER = new User(1, "usertest@email.com", "test", "firstnameTest", "lastnameTest", "USER", BANKACCOUNT);
    
    private final Transaction TRANSACTION =
            new Transaction(1, "955a7b8d-86bf-4e2c-91ce-5d25dbbf5d60", "user2test@email.com", new BigDecimal("150.00"),
                    "First transaction", LocalDateTime.now(), BANKACCOUNT);
    private final Transaction TRANSACTION2 =
            new Transaction(2, "975a7b8d-81bf-4e2c-32ce-5d25dpof5d62", "user2test@email.com", new BigDecimal("250.00"),
                    "Second transaction", LocalDateTime.now(), BANKACCOUNT2);
    private final List<Transaction> TRANSACTIONLIST = new ArrayList<>(List.of(TRANSACTION, TRANSACTION2));
    
    @Test
    void shouldGetAllReturnAllTransactionListTest() {
        
        when(transactionRepository.findAll()).thenReturn(TRANSACTIONLIST);
        
        Iterable<Transaction> result = transactionService.getAll();
        
        Assertions.assertEquals(TRANSACTIONLIST, result);
    }
    
    @Test
    void shouldGetByReturnTransactionTest() {
        
        when(transactionRepository.findById(1)).thenReturn(Optional.of(TRANSACTION));
        
        Optional<Transaction> result = transactionService.getBy(1);
        
        Assertions.assertEquals(Optional.of(TRANSACTION), result);
    }
    
    
    @Test
    @WithMockUser(
            username = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldSaveTransactionWithTransactionDtoTest() {
        TransactionDto transactionDto =
                new TransactionDto("usertest2@email.com", new BigDecimal("250.00"), "Second test", BANKACCOUNT);
        
        when(userService.getTheAuthenticatedUser()).thenReturn(USER);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(TRANSACTION);
        
        Transaction result = transactionService.save(transactionDto);
        
        Assertions.assertEquals(TRANSACTION, result);
    }
    
    @Test
    void shouldDeleteByTest() {
        
        doNothing().when(transactionRepository)
                .deleteById(1);
        
        transactionService.deleteBy(1);
        
        Assertions.assertDoesNotThrow(() -> transactionService.deleteBy(1));
    }
    
    @Test
    void shouldGetLastTransactionByBankAccountIdTest(){
        when(transactionRepository.getTheLastTransactionByBankAccountId(2)).thenReturn(TRANSACTION2);
        
        Transaction result = transactionService.getLastTransactionByBankAccountId(2);
        
        Assertions.assertEquals(TRANSACTION2, result);
    }
    
    @Test
    @WithMockUser(username = "usertest@email.com", password = "test", roles = "USER")
    void shouldReturnPageOfTransactionByBankAccountIdTest(){

        BANKACCOUNT.setTransactions(TRANSACTIONLIST);
        int numbPage = 0;
        int sizePage = 3;
        Pageable pageable = PageRequest.of(numbPage, sizePage);
        Page<Transaction> page = new PageImpl<>(BANKACCOUNT.getTransactions(), pageable, BANKACCOUNT.getTransactions().size());
        
        when(userService.getTheAuthenticatedUser()).thenReturn(USER);
        when(transactionRepository.getPageTransactionsByBankAccountId(1, pageable)).thenReturn(page);
        Page<Transaction> result = transactionService.getTransactionByBankAccountId(numbPage, sizePage);
        
        Assertions.assertEquals(2, result.stream()
                .count());
        Assertions.assertTrue(result.toList().contains(TRANSACTION));
        Assertions.assertTrue(result.toList().contains(TRANSACTION2));
    }
}
