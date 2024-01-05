package com.paymybuddy.controller;

import com.paymybuddy.dto.BuddyDto;
import com.paymybuddy.dtoService.BuddyDtoService;
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

import java.security.Principal;
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
    private BuddyDtoService buddyDtoService;
    
    @MockBean
    private UserService userService;
    @Mock
    private Principal principal;
    
    private final User user =
            new User(2, "usertest@email.com", "$2a$10$8YuJdmjucTcYUnargyHj8u64QuxmQGTNB7cpAIBSw2wYonwyOzDK6",
                    "firstnameTest", "lastnameTest", "USER", new BankAccount());
    
    @Test
    @WithMockUser
    void shouldReturnAddBuddyPageTest() throws Exception {
        mvc.perform(get("/addBuddy"))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser
    void shouldReturnAddBuddyWithErrorEmailTest() throws Exception {
        mvc.perform(get("/addBuddy?errorEmail"))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser
    void shouldReturnAddBuddyWithErrorRelation() throws Exception {
        mvc.perform(get("/addBuddy?errorRelation"))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser
    void shouldReturnAddBuddyErrorSameEmailIfTheBuddyEmailIsTheSameOfPrincipalName() {
        BuddyDto buddyDto = new BuddyDto();
        
        when(buddyDtoService.sameEmailCheck(buddyDto)).thenReturn(true);
        
        String result = addBuddyController.addNewBuddy(buddyDto);
        
        String expectedResult = "redirect:/addBuddy?errorSameEmail";
        
        Assertions.assertEquals(expectedResult, result);
    }
    
    @Test
    @WithMockUser
    void shouldReturnAddBuddyErrorEmailIfCheckIsFalse() {
        BuddyDto buddyDto = new BuddyDto();
        
        when(buddyDtoService.sameEmailCheck(buddyDto)).thenReturn(false);
        
        String result = addBuddyController.addNewBuddy(buddyDto);
        
        String expectedResult = "redirect:/addBuddy?errorEmail";
        
        Assertions.assertEquals(expectedResult, result);
    }
    
    @Test
    @WithMockUser
    void shouldReturnContactIfEmailCheckIsTrueAndRelationAlreadyExistFalse() {
        BuddyDto buddyDto = new BuddyDto();
        
        when(userService.buddyEmailExistCheck(buddyDto)).thenReturn(true);
        when(userService.getPrincipalId(principal)).thenReturn(1);
        String result = addBuddyController.addNewBuddy(buddyDto);
        
        String expectedResult = "redirect:/contact";
        
        Assertions.assertEquals(expectedResult, result);
    }
    
    @Test
    @WithMockUser
    void shouldReturnContactIfEmailCheckIsTrueAndRelationAlreadyExistTrue() {
        BuddyDto buddyDto = new BuddyDto();
        
        when(userService.buddyEmailExistCheck(buddyDto)).thenReturn(true);
        when(userService.getPrincipalId(principal)).thenReturn(1);
        when(userService.buddyRelationAlreadyExist(buddyDto)).thenReturn(true);
        String result = addBuddyController.addNewBuddy(buddyDto);
        
        String expectedResult = "redirect:/addBuddy?errorRelation";
        
        Assertions.assertEquals(expectedResult, result);
    }
    
    @Test
    @WithMockUser
    void shouldReturnContactIfEmailCheckIsTrueAndBuddyIsPresent() {
        BuddyDto buddyDto = new BuddyDto();
        
        when(userService.buddyEmailExistCheck(buddyDto)).thenReturn(true);
        when(userService.getPrincipalId(principal)).thenReturn(1);
        when(userService.getByEmail(buddyDto.getEmail())).thenReturn(Optional.of(user));
        String result = addBuddyController.addNewBuddy(buddyDto);
        
        String expectedResult = "redirect:/contact";
        
        Assertions.assertEquals(expectedResult, result);
    }
}
