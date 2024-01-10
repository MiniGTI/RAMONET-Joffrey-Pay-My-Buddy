package com.paymybuddy.controller;

import com.paymybuddy.dto.PasswordDto;
import com.paymybuddy.dto.UserModifyDto;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.service.TransactionService;
import com.paymybuddy.service.UserService;
import com.paymybuddy.util.InputChecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest
public class ProfileControllerTest {
    
    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private ProfileController profileController;
    
    @MockBean
    private UserService userService;
    
    @MockBean
    private TransactionService transactionService;
    
    @MockBean
    private InputChecker inputChecker;
    
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
    void shouldGetProfilePageTest(){
        when(userService.getTheAuthenticatedUser()).thenReturn(USER);
        when(transactionService.getLastTransactionByBankAccountId(1)).thenReturn(TRANSACTION);
        String result = profileController.profile(model);
        
        Assertions.assertTrue(result.contains("/html/authenticated/profile"));
    }
    
    @Test
    @WithMockUser(
            username = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldModifyProfileTest(){
        UserModifyDto userModifyDto = new UserModifyDto("newFirstname", "newLastname", "newEmail");
        User updatedUser =
                new User(1, "newEmail", "test", "newFirstname", "newLastname", "USER", new BankAccount());
        
        when(userService.getTheAuthenticatedUser()).thenReturn(USER);
        when(userService.save(userModifyDto)).thenReturn(updatedUser);
        
        String result = profileController.profileModify(userModifyDto);
        
        Assertions.assertTrue(result.contains("redirect:/profile?profileSuccess"));
    }
    
    @Test
    @WithMockUser(
            username = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldModifyPasswordIfPasswordModifyCheckIsTrueTest(){
        PasswordDto passwordDto = new PasswordDto("test", "testtest", "testtest");
        User updatedUser =
                new User(1, "newEmail", "testtest", "newFirstname", "newLastname", "USER", new BankAccount());
        
        when(inputChecker.passwordModifyCheck(passwordDto)).thenReturn(true);
        when(userService.save(passwordDto)).thenReturn(updatedUser);
        
        String result = profileController.passwordModify(passwordDto);
        
        Assertions.assertTrue(result.contains("redirect:/profile?passwordSuccess"));
    }
    
    @Test
    @WithMockUser(
            username = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldModifyPasswordIfPasswordModifyCheckIsFalseTest(){
        PasswordDto passwordDto = new PasswordDto("wrong", "testtest", "testtest");
       
        when(inputChecker.passwordModifyCheck(passwordDto)).thenReturn(false);
        
        String result = profileController.passwordModify(passwordDto);
        
        Assertions.assertTrue(result.contains("redirect:/profile?passwordError"));
    }
    
    @Test
    @WithMockUser(
            username = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldDeleteAccountTest(){
        doNothing().when(userService).deleteBy();
        
        String result = profileController.delete();
        
        Assertions.assertTrue(result.contains("redirect:/login?delete"));
    }
}
