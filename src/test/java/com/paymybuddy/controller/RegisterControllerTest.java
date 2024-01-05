package com.paymybuddy.controller;

import com.paymybuddy.dto.UserDto;
import com.paymybuddy.dtoService.UserDtoService;
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
    private UserDtoService userDtoService;
    
    private final UserDto userDto = new UserDto();
    
    @Test
    void shouldReturnRegisterPageTest() throws Exception {
        mvc.perform(get("/register"))
                .andExpect(status().isOk());
    }
    
    @Test
    void shouldReturnRegisterWithErrorEmailParamPageTest() throws Exception {
        mvc.perform(get("/register?errorEmail"))
                .andExpect(status().isOk());
    }
    
    @Test
    void shouldReturnRegisterWithErrorPasswordParamPageTest() throws Exception {
        mvc.perform(get("/register?errorPassword"))
                .andExpect(status().isOk());
    }
    
    @Test
    void shouldReturnRegisterSuccessPageTest() throws Exception {
        mvc.perform(get("/registerSuccess"))
                .andExpect(status().isOk());
    }
    
    @Test
    void shouldReturnRegisterErrorEmailIfEmailCheckIsFalse() {
        when(userDtoService.emailCheck("", "")).thenReturn(false);
        
        String result = registerController.registrationUser(userDto);
        String expectedResult = "redirect:/register?errorEmail";
        
        Assertions.assertEquals(expectedResult, result);
    }
    
    @Test
    void shouldReturnRegisterErrorPasswordIfPasswordCheckIdFalse() {
        UserDto userDto = new UserDto("a", "a", "b", "c", "f", "l", "USER");
        when(userDtoService.emailCheck(userDto.getEmail(), userDto.getEmailCheck())).thenReturn(true);
        when(userDtoService.passwordCheck(userDto.getPassword(), userDto.getPasswordCheck())).thenReturn(false);
        
        String result = registerController.registrationUser(userDto);
        String expectedResult = "redirect:/register?errorPassword";
        
        Assertions.assertEquals(expectedResult, result);
    }
    
    @Test
    void shouldReturnRegisterSuccessIfUserDtoInputIsRight() {
        UserDto userDto = new UserDto("a", "a", "b", "b", "f", "l", "USER");
        when(userDtoService.emailCheck(userDto.getEmail(), userDto.getEmailCheck())).thenReturn(true);
        when(userDtoService.passwordCheck(userDto.getPassword(), userDto.getPasswordCheck())).thenReturn(true);
        
        String result = registerController.registrationUser(userDto);
        String expectedResult = "redirect:/registerSuccess";
        
        Assertions.assertEquals(expectedResult, result);
    }
}
