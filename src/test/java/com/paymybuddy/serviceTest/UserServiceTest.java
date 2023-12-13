package com.paymybuddy.serviceTest;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Test class for the UserService.
 */
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {
    /**
     * UserService call.
     */
    @Autowired
    private UserService userService;
    /**
     * UserRepository mock.
     */
    @MockBean
    private UserRepository userRepository;
    /**
     * A simple User object initialization for test.
     */
    private final User USER = new User("savetest@gmail.com", "passTest", "first", "last", new BankAccount());
    /**
     * Test of the save() method of UserService.
     */
    @Test
    void userServiceSaveTest() {
        when(userRepository.save(USER)).thenReturn(USER);
        
        User result = userService.save(USER);
        
        Assertions.assertEquals(USER, result);
    }
    /**
     * Test of the GetBy() method of UserService.
     */
    @Test
    void userServiceGetByIdTest() {
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(USER));
        
        Optional<User> result = userService.getBy(1);
        
        Assertions.assertEquals(USER.getEmail(), result.get()
                .getEmail());
    }
    /**
     * Test of the GetAll() method of UserService.
     */
    @Test
    void userServiceGetAllTest() {
        List<User> users = new ArrayList<>();
        users.add(USER);
        
        when(userRepository.findAll()).thenReturn(users);
        
        Iterable<User> result = userService.getAll();
        
        int count = 0;
        
        for(User u : result) {
            count++;
        }
        
        Assertions.assertEquals(users.size(), count);
    }
    /**
     * Test of the DeleteBy() method of UserService.
     */
    @Test
    void userServiceDeleteByTest() {
        
        doNothing().when(userRepository)
                .deleteById(1);
        userService.deleteBy(1);
        
        Assertions.assertDoesNotThrow(() -> userService.deleteBy(1));
    }
}
