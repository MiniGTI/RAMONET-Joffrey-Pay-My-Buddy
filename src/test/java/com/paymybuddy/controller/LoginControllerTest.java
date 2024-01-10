package com.paymybuddy.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {
    
    @Autowired
    private MockMvc mvc;
    
    @Test
    void shouldReturnTheLoginPageTest() throws Exception {
        mvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    
    @Test
    void shouldReturnUserLoginWithErrorParamWhenUserUseBadIdTest() throws Exception {
        mvc.perform(formLogin("/login").user("user")
                        .password("password"))
                .andExpect(unauthenticated());
    }
    
}
