package com.paymybuddy.controller;

import com.paymybuddy.dto.UserDto;
import com.paymybuddy.dto.UserDtoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class RegisterControllerTest {
    
    @Autowired
    private MockMvc mvc;
    
    @MockBean
    private UserDtoService userDtoService;
    
    private final UserDto
            userDto = new UserDto("usertest3@email.com", "wrong3@email.com", "pass", "wrongpass", "firstname3test", "lastname3test", "USER");
    
    @Test
    public void shouldReturnRegisterPageTest() throws Exception {
        mvc.perform(get("/register"))
                .andExpect(status().isOk());
    }
    
    @Test
    public void shouldReturnRegisterWithErrorEmailParamPageTest() throws Exception{
        mvc.perform(get("/register?errorEmail")).andExpect(status().isOk());
    }
    
    @Test
    public void shouldReturnRegisterWithErrorPasswordParamPageTest() throws Exception{
        mvc.perform(get("/register?errorPassword")).andExpect(status().isOk());
    }
    @Test
    public void shouldReturnRegisterSuccessPageTest() throws Exception{
        mvc.perform(get("/registerSuccess")).andExpect(status().isOk());
    }
    
}
