package com.paymybuddy.controller;

import com.paymybuddy.dto.UserDto;
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
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class RegisterControllerTest {
    
    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private RegisterController registerController;
    
    @MockBean
    private InputChecker inputChecker;
    
    @MockBean
    private UserService userService;
    private final User USER =
            new User(1, "usertest@email.com", "test", "firstnameTest", "lastnameTest", "USER", new BankAccount());
    
    
    private final UserDto userDto = new UserDto("usertest@email.com", "usertest@email.com", "test", "test", "firstname", "lastname");
    
    @Test
    void shouldReturnRegisterPageTest() throws Exception {
        mvc.perform(get("/register"))
                .andExpect(status().isOk());
    }
    
    @Test
    void shouldReturnRegisterErrorEmailAlreadyExistWhenEmailAlreadyRegisterTest() {
        when(userService.getByEmail(userDto.getEmail())).thenReturn(Optional.of(USER));
        
        String result = registerController.registrationUser(userDto);
        String expectedResult = "redirect:/register?errorEmailAlreadyExist";
        
        Assertions.assertEquals(expectedResult, result);
    }
    
    @Test
    void shouldReturnRegisterErrorEmailWhenEmailCheckIsFalseTest() {
        when(userService.getByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(inputChecker.sameInputCheck("1", "2")).thenReturn(false);
        
        String result = registerController.registrationUser(userDto);
        String expectedResult = "redirect:/register?errorEmail";
        
        Assertions.assertEquals(expectedResult, result);
    }
    
    @Test
    void shouldReturnRegisterErrorPasswordWhenPasswordCheckIdFalseTest() {
        when(userService.getByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(inputChecker.sameInputCheck(userDto.getEmail(), userDto.getEmailCheck())).thenReturn(true);
        when(inputChecker.sameInputCheck(userDto.getPassword(), userDto.getPasswordCheck())).thenReturn(false);
        
        String result = registerController.registrationUser(userDto);
        String expectedResult = "redirect:/register?errorPassword";
        
        Assertions.assertEquals(expectedResult, result);
    }
    
    @Test
    void shouldReturnRegisterSuccessIfUserDtoInputIsRightTest() {
        UserDto userDto = new UserDto("test@test.com", "test@test.com", "test", "test", "firstname", "lastname");
        when(userService.getByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(inputChecker.sameInputCheck(userDto.getEmail(), userDto.getEmailCheck())).thenReturn(true);
        when(inputChecker.sameInputCheck(userDto.getPassword(), userDto.getPasswordCheck())).thenReturn(true);
        
        String result = registerController.registrationUser(userDto);
        String expectedResult = "redirect:/registerSuccess";
        
        Assertions.assertEquals(expectedResult, result);
    }
}
