package com.paymybuddy.controller;

import com.paymybuddy.dto.UserDto;
import com.paymybuddy.util.InputChecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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
    
    private final UserDto userDto = new UserDto();
    
    @Test
    void shouldReturnRegisterPageTest() throws Exception {
        mvc.perform(get("/register"))
                .andExpect(status().isOk());
    }
    
    
    @Test
    void shouldReturnRegisterErrorEmailWhenEmailCheckIsFalse() {
        when(inputChecker.sameInputCheck("1", "2")).thenReturn(false);
        
        String result = registerController.registrationUser(userDto);
        String expectedResult = "redirect:/register?errorEmail";
        
        Assertions.assertEquals(expectedResult, result);
    }
    
    @Test
    void shouldReturnRegisterErrorPasswordWhenPasswordCheckIdFalse() {
        UserDto userDto = new UserDto("test@test.com", "test@test.com", "test", "testtest", "firstname", "lastname");
        
        when(inputChecker.sameInputCheck(userDto.getEmail(), userDto.getEmailCheck())).thenReturn(true);
        when(inputChecker.sameInputCheck(userDto.getPassword(), userDto.getPasswordCheck())).thenReturn(false);
        
        String result = registerController.registrationUser(userDto);
        String expectedResult = "redirect:/register?errorPassword";
        
        Assertions.assertEquals(expectedResult, result);
    }
    
    @Test
    void shouldReturnRegisterSuccessIfUserDtoInputIsRight() {
        UserDto userDto = new UserDto("test@test.com", "test@test.com", "test", "test", "firstname", "lastname");
        
        when(inputChecker.sameInputCheck(userDto.getEmail(), userDto.getEmailCheck())).thenReturn(true);
        when(inputChecker.sameInputCheck(userDto.getPassword(), userDto.getPasswordCheck())).thenReturn(true);
        
        String result = registerController.registrationUser(userDto);
        String expectedResult = "redirect:/registerSuccess";
        
        Assertions.assertEquals(expectedResult, result);
    }
}
