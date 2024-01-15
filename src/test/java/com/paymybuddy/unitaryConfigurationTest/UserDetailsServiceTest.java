package com.paymybuddy.unitaryConfigurationTest;

import com.paymybuddy.configuration.CustomUserDetailsService;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
public class UserDetailsServiceTest {
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @MockBean
    private UserRepository userRepository;
    
    private final User USER =
            new User(1, "usertest@email.com", "test", "firstnameTest", "lastnameTest", "USER", new BankAccount());
    
    @Test
    void shouldLoadUserByUsernameWhenUserIsNotFoundTest(){
        when(userRepository.findByEmail("email")).thenReturn(Optional.empty());
        
        UsernameNotFoundException usernameNotFoundException = Assertions.assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("email"));
        
        Assertions.assertTrue("User not found.".contains(usernameNotFoundException.getMessage()));
    }
    
    @Test
    void shouldLoadUserByUsernameWhenUserIsFoundTest(){
        when(userRepository.findByEmail(USER.getEmail())).thenReturn(Optional.of(USER));
        
        UserDetails result = customUserDetailsService.loadUserByUsername(USER.getEmail());
        
        Assertions.assertEquals(USER.getEmail(), result.getUsername());
        Assertions.assertEquals(USER.getPassword(), result.getPassword());
    }
}
