package com.paymybuddy.Service;

import com.paymybuddy.dto.UserDto;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    
    @Autowired
    private UserService userService;
    
    @MockBean
    private UserRepository userRepository;
    
    private final User user1 =
            new User("usertest@email.com", "$2a$10$8YuJdmjucTcYUnargyHj8u64QuxmQGTNB7cpAIBSw2wYonwyOzDK6",
                    "firstnameTest", "lastnameTest", "USER", new BankAccount());
    
    private final List<User> userList = new ArrayList<>(List.of(user1,
            new User("user2test@email.com", "$2a$10$8YuJdmjucTcYUnargyHj8u64QuxmQGTNB7cpAIBSw2wYonwyOzDK6",
                    "firstname2Test", "lastname2Test", "USER", new BankAccount())));
    
    private final Optional<User> optUser = Optional.of(user1);
    
    @Mock
    private Principal principal;
    
    @Test
    public void getAllTest() {
        
        when(userRepository.findAll()).thenReturn(userList);
        
        Iterable<User> result = userService.getAll();
        
        int resultSize = 0;
        
        for(User u : result) {
            resultSize++;
        }
        ;
        
        Assertions.assertEquals(userList.size(), resultSize);
    }
    
    @Test
    public void getByTest() {
        
        when(userRepository.findById(1)).thenReturn(optUser);
        
        Optional<User> result = userService.getBy(1);
        
        Assertions.assertEquals(optUser, result);
    }
    
    @Test
    public void getByEmailTest() {
        
        when(userRepository.findByEmail("usertest@email.com")).thenReturn(optUser);
        
        Optional<User> result = userService.getByEmail("usertest@email.com");
        
        Assertions.assertEquals(optUser, result);
    }
    
    @Test
    public void deleteByTest() {
        
        doNothing().when(userRepository)
                .deleteById(1);
        
        userService.deleteBy(1);
        
        Assertions.assertDoesNotThrow(() -> userService.deleteBy(1));
    }
    
    @Test
    @Disabled
    public void saveTest() {
        UserDto userDto = new UserDto("usertest3@email.com", "usertest3@email.com", "pass", "pass", "firstname3test",
                "lastname3test", "USER");
        
        User user = new User("usertest3@email.com", "$2a$10$Eo98fWclFN30hUnDFyPh2u3DHGVF3Q7OColEHSV8Sk/tuI4ewwwFu",
                "firstname3test", "lastname3test", "USER", new BankAccount());
        when(userRepository.save(user)).thenReturn(user);
        
        User result = userService.save(userDto);
        
        Assertions.assertEquals(user, result);
    }
    
    @Test
    void getPrincipalIdTest() {
        when(userRepository.findByEmail(principal.getName())).thenReturn(Optional.of(user1));
        
        Integer result = userService.getPrincipalId(principal);
        
        Assertions.assertEquals(user1.getId(), result);
    }
    
    @Test
    void getPrincipalIdWhenPrincipalIsNull() {
        when(userRepository.findByEmail(principal.getName())).thenReturn(Optional.empty());
        
        Integer result = userService.getPrincipalId(principal);
        
        Assertions.assertNull(result);
    }
}
