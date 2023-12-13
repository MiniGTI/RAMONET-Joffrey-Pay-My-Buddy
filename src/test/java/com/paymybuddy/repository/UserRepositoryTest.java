package com.paymybuddy.repository;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

/**
 * Test class for the UserRepository.
 */
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {
    /**
     * UserRepository call.
     */
    @Autowired
    private UserRepository userRepository;
    
    /**
     * A simple User object initialization for test.
     */
    private final User USER = new User("savetest@gmail.com", "passTest", "first", "last", new BankAccount());
    
    /**
     * Test of the save() method of UserRepository.
     */
    @Test
    @Order(1)
    void saveUserRepositoryTest() {
        User userSaved = userRepository.save(USER);
        
        Assertions.assertEquals(USER, userSaved);
    }
    
    /**
     * Test of the findBy() method of UserRepository.
     */
    @Test
    @Order(2)
    void findByIdUserRepositoryTest() {
        Optional<User> userFound = userRepository.findById(1);
        
        Assertions.assertEquals("test@gmail.com", userFound.get()
                .getEmail());
    }
    
    /**
     * Test of the findAll() method of UserRepository.
     */
    @Test
    @Order(3)
    void findAllUserRepositoryTest() {
        Iterable<User> users = userRepository.findAll();
        
        int counter = 0;
        for(User u : users) {
            counter++;
        }
        
        Assertions.assertEquals(3, counter);
    }
    
    /**
     * Test of the delete() method of UserRepository.
     */
    @Test
    @Order(4)
    void deleteByEmailUserRepositoryTest() {
        userRepository.deleteById(3);
        
        Iterable<User> users = userRepository.findAll();
        
        int counter = 0;
        for(User u : users) {
            counter++;
        }
        
        Assertions.assertEquals(2, counter);
    }
}
