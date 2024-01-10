package com.paymybuddy.controller;

import com.paymybuddy.dto.BuddyDto;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.service.UserService;
import com.paymybuddy.util.InputChecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AddBuddyControllerTest {
    
    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private AddBuddyController addBuddyController;
    
    @MockBean
    private UserService userService;
    
    @MockBean
    private InputChecker inputChecker;

    @Test
    @WithMockUser
    void shouldReturnAddBuddyPageTest() throws Exception {
        mvc.perform(get("/addBuddy"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturnAddBuddyErrorSameEmailIfTheBuddyEmailIsTheSameOfPrincipalName() {
        BuddyDto buddyDto = new BuddyDto();
        
        when(inputChecker.sameEmailCheck(buddyDto)).thenReturn(true);
        
        String result = addBuddyController.addNewBuddy(buddyDto);
        
        String expectedResult = "redirect:/addBuddy?errorSameEmail";
        
        Assertions.assertEquals(expectedResult, result);
    }
    
    @Test
    @WithMockUser
    void shouldReturnAddBuddyErrorEmailIfCheckIsFalse() {
        BuddyDto buddyDto = new BuddyDto();
        
        when(inputChecker.sameEmailCheck(buddyDto)).thenReturn(false);
        
        String result = addBuddyController.addNewBuddy(buddyDto);
        
        String expectedResult = "redirect:/addBuddy?errorEmail";
        
        Assertions.assertEquals(expectedResult, result);
    }
    
    @Test
    @WithMockUser
    void shouldReturnContactIfEmailCheckIsTrueAndRelationAlreadyExistFalse() {
        BuddyDto buddyDto = new BuddyDto();
        
        when(inputChecker.buddyEmailExistCheck(buddyDto)).thenReturn(true);
        String result = addBuddyController.addNewBuddy(buddyDto);
        
        String expectedResult = "redirect:/contact";
        
        Assertions.assertEquals(expectedResult, result);
    }
    
    @Test
    @WithMockUser
    void shouldReturnContactIfEmailCheckIsTrueAndRelationAlreadyExistTrue() {
        BuddyDto buddyDto = new BuddyDto();
        
        when(inputChecker.buddyEmailExistCheck(buddyDto)).thenReturn(true);
        when(inputChecker.buddyRelationAlreadyExist(buddyDto)).thenReturn(true);
        String result = addBuddyController.addNewBuddy(buddyDto);
        
        String expectedResult = "redirect:/addBuddy?errorRelation";
        
        Assertions.assertEquals(expectedResult, result);
    }
    
    @Test
    @WithMockUser
    void shouldReturnContactIfEmailCheckIsTrueAndBuddyIsPresent() {
        BuddyDto buddyDto = new BuddyDto();
        User user =
                new User(2, "usertest@email.com", "$2a$10$8YuJdmjucTcYUnargyHj8u64QuxmQGTNB7cpAIBSw2wYonwyOzDK6",
                        "firstnameTest", "lastnameTest", "USER", new BankAccount());
        
        when(inputChecker.buddyEmailExistCheck(buddyDto)).thenReturn(true);
        when(userService.getByEmail(buddyDto.getEmail())).thenReturn(Optional.of(user));
        String result = addBuddyController.addNewBuddy(buddyDto);
        
        String expectedResult = "redirect:/contact";
        
        Assertions.assertEquals(expectedResult, result);
    }
}
