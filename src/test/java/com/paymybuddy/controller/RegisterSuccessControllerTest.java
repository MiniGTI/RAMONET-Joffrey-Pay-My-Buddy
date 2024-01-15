package com.paymybuddy.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegisterSuccessControllerTest {
    
    @Autowired
    private MockMvc mvc;
    
    @Test
    void shouldReturnRegisterSuccessPageTest() throws Exception {
        mvc.perform(get("/registerSuccess"))
                .andExpect(status().isOk());
    }
}
