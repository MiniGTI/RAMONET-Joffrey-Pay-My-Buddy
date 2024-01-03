package com.paymybuddy.dto;

import com.paymybuddy.dtoService.UserDtoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserDtoServiceTest {
    
    @Autowired
    private UserDtoService userDtoService;
    
    private final String password = "passtest";
    private final String email = "email@test.com";
    private final String wrongInput = "wrongtest";
    
    @Test
    public void passwordCheckShouldReturnTrueIfInputsAreEqualsTest() {
        Boolean result = userDtoService.passwordCheck(password, password);
        
        Assertions.assertTrue(result);
    }
    
    @Test
    public void passwordCheckShouldReturnFalseIfInputsAreEqualsTest() {
        Boolean result = userDtoService.passwordCheck(password, wrongInput);
        
        Assertions.assertFalse(result);
    }
    
    @Test
    public void emailCheckShouldReturnTrueIfInputsAreEqualsTest() {
        Boolean result = userDtoService.emailCheck(email, email);
        
        Assertions.assertTrue(result);
    }
    
    @Test
    public void emailCheckShouldReturnFalseIfInputsAreEqualsTest() {
        Boolean result = userDtoService.emailCheck(email, wrongInput);
        
        Assertions.assertFalse(result);
    }
}
