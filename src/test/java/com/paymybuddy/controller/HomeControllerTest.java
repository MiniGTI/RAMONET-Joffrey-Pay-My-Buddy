package com.paymybuddy.controller;

import com.paymybuddy.dto.BankAccountDto;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.service.UserService;
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
import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTest {
    
    @Autowired
    private MockMvc mvc;
    @Autowired
    private HomeController homeController;
    @MockBean
    private UserService userService;
    @Mock
    private Model model;
 
    private final BankAccount BANKACCOUNT =
            new BankAccount(1, new BigDecimal("1500.00"), "67acb3ed-6d46-4a15-ad02-d0d0be604fc1",
                    "6e3429a6-f593-45dc-8a93-1c9c3c4f32da", new ArrayList<>());

    private final User USER =
            new User(1, "usertest@email.com", "test", "firstnameTest", "lastnameTest", "USER", BANKACCOUNT);


    
    @Test
    @WithMockUser(
            username = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldReturnHomePageTest() throws Exception {

        when(userService.getTheAuthenticatedUser()).thenReturn(USER);

        String result = homeController.accountField(model);
        
        Assertions.assertTrue(result.contains("/html/authenticated/home"));
        mvc.perform(get("/")).andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(
            username = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldDepositTest(){
        BankAccountDto bankAccountDto = new BankAccountDto(new BigDecimal("150.00"));
        
        when(userService.getTheAuthenticatedUser()).thenReturn(USER);
        
        String result = homeController.deposite(bankAccountDto);
        
        Assertions.assertTrue(result.contains("redirect:/"));
    }
    }
