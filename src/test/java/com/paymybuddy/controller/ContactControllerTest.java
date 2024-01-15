package com.paymybuddy.controller;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactControllerTest {
    
    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private ContactController contactController;
    
    @MockBean
    private UserService userService;
    
    private final User USER =
            new User(1, "usertest@email.com", "test", "firstnameTest", "lastnameTest", "USER", new BankAccount());
    private final User USER2 =
            new User(2, "user2test@email.com", "test", "firstname2Test", "lastname2Test", "USER", new BankAccount());
    
    private final List<User> USERLIST = new ArrayList<>(List.of(USER2));
    
    @BeforeEach
    void setUpPerTest() {
        USER.setBuddys(USERLIST);
    }
    
    @Test
    @WithMockUser(
            username = "usertest@test.com",
            password = "test",
            roles = "USER")
    void shouldReturnContactPageTest() throws Exception {
        int numPage = 0;
        int pageSize = 3;
        Pageable pageable = PageRequest.of(numPage, pageSize);
        Page<User> page = new PageImpl<>(USER.getBuddys(), pageable, USER.getBuddys()
                .size());
        when(userService.getPageBuddyById(numPage, pageSize)).thenReturn(page);
        
        Assertions.assertTrue(page.toList()
                .contains(USER2));
        mvc.perform(get("/contact"))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(
            username = "usertest@tes.com",
            password = "test",
            roles = "USER")
    void shouldDeleteBuddyTest() {
        List<User> buddys = USER.getBuddys();
        buddys.remove(USER2);
        USER.setBuddys(buddys);
        
        when(userService.deleteBuddy(2)).thenReturn(USER);
        
        String result = contactController.deleteBuddy(2);
        
        Assertions.assertTrue(result.contains("redirect:/contact"));
    }
}
