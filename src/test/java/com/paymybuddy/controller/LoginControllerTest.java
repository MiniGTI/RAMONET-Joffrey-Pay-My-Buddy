package com.paymybuddy.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
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
    public void shouldReturnTheLoginPage() throws Exception {
        mvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser
    public void userLoginWithGoodIdsTest() throws Exception {
        mvc.perform(formLogin("/login").user("user")
                        .password("password"))
                .andExpect(authenticated());
    }
    
    @Test
    public void userLoginWithBadIdsTest() throws Exception {
        mvc.perform(formLogin("/login").user("user")
                        .password("password"))
                .andExpect(unauthenticated());
    }
    
    @Test
    @WithMockUser
    public void shouldReturnHomePage() throws Exception {
        mvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
