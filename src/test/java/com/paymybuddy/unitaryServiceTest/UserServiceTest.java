package com.paymybuddy.unitaryServiceTest;

import com.paymybuddy.dto.BuddyDto;
import com.paymybuddy.dto.PasswordDto;
import com.paymybuddy.dto.UserDto;
import com.paymybuddy.dto.UserModifyDto;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    
    @Autowired
    private UserService userService;
    
    @MockBean
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    
    @MockBean
    private BCryptPasswordEncoder passwordEncoderMock;
    private final String password = passwordEncoder.encode("test");
    
    private final User USER =
            new User(1, "usertest@email.com", password, "firstnameTest", "lastnameTest", "USER", new BankAccount());
    private final User USER2 =
            new User(2, "user2test@email.com", password, "firstname2Test", "lastname2Test", "USER", new BankAccount());
    
    private final List<User> USERLIST = new ArrayList<>(List.of(USER, USER2));
    
    @Test
    void shouldGetAllReturnAllUserListTest() {
        when(userRepository.findAll()).thenReturn(USERLIST);
        
        Iterable<User> result = userService.getAll();
        
        Assertions.assertEquals(USERLIST, result);
    }
    
    @Test
    void shouldGetByReturnUserTest() {
        when(userRepository.findById(1)).thenReturn(Optional.of(USER));
        
        Optional<User> result = userService.getBy(1);
        
        Assertions.assertEquals(Optional.of(USER), result);
    }
    
    @Test
    void shouldGetByEmailReturnUserTest() {
        when(userRepository.findByEmail("usertest@email.com")).thenReturn(Optional.of(USER));
        
        Optional<User> result = userService.getByEmail("usertest@email.com");
        
        Assertions.assertEquals(Optional.of(USER), result);
    }
    
    @Test
    void shouldSaveUserWithUserDtoTest() {
        UserDto userDto = new UserDto("usertest@email.com", "usertest@email.com", "test", "test", "firstnameTest",
                "lastnameTest");
        
        when(userRepository.save(any(User.class))).thenReturn(USER);
        
        User result = userService.save(userDto);
        
        Assertions.assertEquals(USER, result);
    }
    
    @Test
    @WithMockUser(
            username = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldSaveWithUserModifyWhenOnlyFirstnameUpdatedTest() {
        UserModifyDto userModifyDto = new UserModifyDto("newFirstname", "");
        User updatedUser =
                new User(1, "usertest@email.com", password, "newFirstname", "lastnameTest", "USER", new BankAccount());
        
        when(userService.getByEmail("usertest@email.com")).thenReturn(Optional.of(USER));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        
        User result = userService.save(userModifyDto);
        
        Assertions.assertEquals(updatedUser, result);
    }
    
    @Test
    @WithMockUser(
            username = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldSaveWithUserModifyWhenOnlyLastnameUpdatedTest() {
        UserModifyDto userModifyDto = new UserModifyDto("", "newLastname");
        User updatedUser =
                new User(1, "usertest@email.com", password, "firstnameTest", "newLastname", "USER", new BankAccount());
        
        when(userService.getByEmail("usertest@email.com")).thenReturn(Optional.of(USER));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        
        User result = userService.save(userModifyDto);
        Assertions.assertEquals(updatedUser, result);
    }

    @Test
    @WithMockUser(
            username = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldSaveWithUserModifyWhenAllAreUpdatedTest() {
        UserModifyDto userModifyDto = new UserModifyDto("newLastname", "newLastname");
        User updatedUser =
                new User(1, "test@test.com", password, "newFirstname", "newLastname", "USER", new BankAccount());
        
        when(userService.getByEmail("usertest@email.com")).thenReturn(Optional.of(USER));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        
        User result = userService.save(userModifyDto);
        Assertions.assertEquals(updatedUser, result);
    }
    
    @Test
    @WithMockUser(
            username = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldDeleteByReturnNothingTest() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        
        when(userRepository.findByEmail(authentication.getName())).thenReturn(Optional.of(USER));
        doNothing().when(userRepository)
                .deleteById(USER.getId());
        
        userService.deleteBy();
        Assertions.assertDoesNotThrow(() -> userService.deleteBy());
    }
    
    @Test
    @WithMockUser(
            username = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldSaveWithPasswordDtoWhenPasswordDtoIsNotEmptyTest() {
        PasswordDto passwordDto = new PasswordDto("test", "testtest", "testtest");
        String newPasswordEncoded = passwordEncoder.encode("testtest");
        User updatedUser = new User(1, "newtest@test.com", newPasswordEncoded, "newFirstname", "newLastname", "USER",
                new BankAccount());
        
        when(passwordEncoderMock.encode(passwordDto.getNewPassword())).thenReturn(newPasswordEncoded);
        when(userService.getByEmail("usertest@email.com")).thenReturn(Optional.of(USER));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        
        User result = userService.save(passwordDto);
        System.out.println(result);
        Assertions.assertEquals(updatedUser, result);
    }
    
    @Test
    @WithMockUser(
            username = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldSaveWithPasswordDtoWhenPasswordDtoIsEmptyTest() {
        PasswordDto passwordDto = new PasswordDto("test", "", "");
        
        when(userService.getByEmail("usertest@email.com")).thenReturn(Optional.of(USER));
        
        RuntimeException runtimeException =
                Assertions.assertThrows(RuntimeException.class, () -> userService.save(passwordDto));
        
        Assertions.assertTrue("New password not found".contains(runtimeException.getMessage()));
    }
    
    @Test
    @WithMockUser(
            value = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldSaveNewBuddyTest() {
        BuddyDto buddyDto = new BuddyDto("user2test@email.com");
        User updatedUser =
                new User(1, "usertest@email.com", password, "firstnameTest", "lastnameTest", "USER", new BankAccount(),
                        new ArrayList<>(List.of(USER2)), new ArrayList<>());
        
        when(userService.getByEmail("usertest@email.com")).thenReturn(Optional.of(USER));
        when(userService.getByEmail("user2test@email.com")).thenReturn(Optional.of(USER2));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        
        User result = userService.saveNewBuddy(buddyDto);
        
        Assertions.assertEquals(updatedUser, result);
    }
    
    @Test
    @WithMockUser(
            username = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldGetTheAuthenticatedUserIfUserFindTest() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        when(userRepository.findByEmail(authentication.getName())).thenReturn(Optional.of(USER));
        
        User authenticatedUser = userService.getTheAuthenticatedUser();
        
        Assertions.assertEquals(USER, authenticatedUser);
    }
    
    @Test
    @WithMockUser(
            username = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldGetTheAuthenticatedUserIfUserNotFoundTest() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        when(userRepository.findByEmail(authentication.getName())).thenReturn(Optional.empty());
        
        RuntimeException runtimeException =
                Assertions.assertThrows(RuntimeException.class, () -> userService.getTheAuthenticatedUser());
        
        Assertions.assertTrue("Problem to get the Principal.".contains(runtimeException.getMessage()));
    }
    
    @Test
    @WithMockUser(
            username = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldGetAllBuddyTest() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        USER.setBuddys(new ArrayList<>(List.of(USER2)));
        
        when(userRepository.findByEmail(authentication.getName())).thenReturn(Optional.of(USER));
        
        List<User> result = userService.getAllBuddy();
        
        Assertions.assertEquals(USER.getBuddys(), result);
    }
    
    @Test
    @WithMockUser(
            username = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldGetPageBuddyByIdTest() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        USER.setBuddys(new ArrayList<>(List.of(USER2)));
        int numbPage = 0;
        int sizePage = 3;
        Pageable pageable = PageRequest.of(numbPage, sizePage);
        Page<User> page = new PageImpl<>(USER.getBuddys(), pageable, USER.getBuddys()
                .size());
        
        when(userRepository.findByEmail(authentication.getName())).thenReturn(Optional.of(USER));
        when(userRepository.getPageBuddyById(1, pageable)).thenReturn(page);
        Page<User> result = userService.getPageBuddyById(numbPage, sizePage);
        
        Assertions.assertEquals(1, result.stream()
                .count());
        Assertions.assertTrue(result.toList()
                .contains(USER2));
    }
    
    
    @Test
    @WithMockUser(
            username = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldDeleteBuddyWhenBuddyIsFoundTest() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        User updatedUser =
                new User(1, "usertest@email.com", password, "firstnameTest", "lastnameTest", "USER", new BankAccount(),
                        new ArrayList<>(List.of(USER2)), new ArrayList<>());
        USER.setBuddys(new ArrayList<>(List.of(USER2)));
        
        when(userService.getBy(2)).thenReturn(Optional.of(USER2));
        when(userRepository.findByEmail(authentication.getName())).thenReturn(Optional.of(USER));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        
        User result = userService.deleteBuddy(2);
        
        Assertions.assertEquals(updatedUser.getBuddys(), result.getBuddys());
    }
    
    @Test
    @WithMockUser(
            username = "usertest@email.com",
            password = "test",
            roles = "USER")
    void shouldDeleteBuddyWhenBuddyIsNotFoundTest() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        
        when(userService.getBy(2)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(authentication.getName())).thenReturn(Optional.of(USER));
        
        RuntimeException runtimeException =
                Assertions.assertThrows(RuntimeException.class, () -> userService.deleteBuddy(2));
        
        Assertions.assertTrue("Buddy not found.".contains(runtimeException.getMessage()));
    }
    
    @Test
    void shouldGetUserByBuddyDtoWhenTheEmailIsRegisterTest() {
        BuddyDto buddyDto = new BuddyDto("usertest@email.com");
        
        when(userRepository.findByEmail("usertest@email.com")).thenReturn(Optional.of(USER));
        
        User result = userService.getUserByBuddyDto(buddyDto);
        
        Assertions.assertEquals(USER, result);
    }
    
    @Test
    void shouldGetUserByBuddyDtoWhenTheEmailIsNotRegisterTest() {
        BuddyDto buddyDto = new BuddyDto("usertest@email.com");
        
        when(userRepository.findByEmail("usertest@email.com")).thenReturn(Optional.empty());
        
        RuntimeException runtimeException =
                Assertions.assertThrows(RuntimeException.class, () -> userService.getUserByBuddyDto(buddyDto));
        
        Assertions.assertTrue("Buddy not found.".contains(runtimeException.getMessage()));
    }
}
