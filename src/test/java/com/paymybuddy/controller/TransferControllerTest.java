package com.paymybuddy.controller;

import com.paymybuddy.dto.TransactionDto;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.service.BankAccountService;
import com.paymybuddy.service.TransactionService;
import com.paymybuddy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest
public class TransferControllerTest {
    
    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private TransferController transferController;
    
    @MockBean
    private TransactionService transactionService;
    @MockBean
    private BankAccountService bankAccountService;
    
    @MockBean
    private UserService userService;
    
    @Mock
    private Model model;
    private final BankAccount BANKACCOUNT =
            new BankAccount(1, new BigDecimal("2000.00"), "67acb3ed-6d46-4a15-ad02-d0d0be604fc1",
                    "6e3429a6-f593-45dc-8a93-1c9c3c4f32da", new ArrayList<>());
    private final User USER =
            new User(1, "usertest@email.com", "test", "firstnameTest", "lastnameTest", "USER", new BankAccount());
    private final User USER2 =
            new User(2, "user2test@email.com", "test", "firstname2Test", "lastname2Test", "USER", new BankAccount());
    
    private final Transaction TRANSACTION =
            new Transaction(1, "955a7b8d-86bf-4e2c-91ce-5d25dbbf5d60", "user2test@email.com", new BigDecimal("150.00"),
                    "First transaction", LocalDateTime.now(), BANKACCOUNT);
    private final Transaction TRANSACTION2 =
            new Transaction(2, "975a7b8d-81bf-4e2c-32ce-5d25dpof5d62", "user2test@email.com", new BigDecimal("250.00"),
                    "Second transaction", LocalDateTime.now(), BANKACCOUNT);
    
    @Test
    @WithMockUser(
            username = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldGetTransferPageTest(){
        
        List<Transaction> transactionList = new ArrayList<>(List.of(TRANSACTION, TRANSACTION2));
        BANKACCOUNT.setTransactions(transactionList);
        List<User> buddyList = new ArrayList<>(List.of(USER2));
        USER.setBuddys(buddyList);
        int numPage = 0;
        int pageSize = 3;
        Pageable pageable = PageRequest.of(numPage, pageSize);
        Page<Transaction> page = new PageImpl<>(BANKACCOUNT.getTransactions(), pageable, BANKACCOUNT.getTransactions()
                .size());
        
        when(userService.getAllBuddy()).thenReturn(USER.getBuddys());
        when(transactionService.getTransactionByBankAccountId(numPage, pageSize)).thenReturn(page);
        
        String result = transferController.transfer(model, numPage);
        
        Assertions.assertTrue(page.toList()
                .contains(TRANSACTION));
        Assertions.assertTrue(result.contains("/html/authenticated/transfer"));
    }
    
    @Test
    @WithMockUser(
            username = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldReturnErrorBalanceParamWhenConnexionAbleToDepositIsFalseTest() {
        TransactionDto transactionDto = new TransactionDto("user2test@email.com", new BigDecimal("5500.00"), "First transaction",BANKACCOUNT);
        
        when(bankAccountService.ableToDeposit(transactionDto)).thenReturn(false);
        
        String result = transferController.connexion(transactionDto);
        
        Assertions.assertTrue(result.contains("redirect:/transfer?errorBalance"));
    }
    
    @Test
    @WithMockUser(
            username = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldReturnError0BalanceParamWhenConnexionGetAmountIsFalseTest() {
        TransactionDto transactionDto = new TransactionDto("user2test@email.com", new BigDecimal("0"), "First transaction",BANKACCOUNT);
        
        when(bankAccountService.ableToDeposit(transactionDto)).thenReturn(true);
        
        String result = transferController.connexion(transactionDto);
        
        Assertions.assertTrue(result.contains("redirect:/transfer?error0Balance"));
    }
    
    @Test
    @WithMockUser(
            username = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldReturnSuccessParamWhenConnexionTest() {
        TransactionDto transactionDto = new TransactionDto("user2test@email.com", new BigDecimal("110.00"), "First transaction",BANKACCOUNT);
        
        when(bankAccountService.ableToDeposit(transactionDto)).thenReturn(true);
        
        String result = transferController.connexion(transactionDto);
        
        Assertions.assertTrue(result.contains("redirect:/transfer?success"));
    }
}
