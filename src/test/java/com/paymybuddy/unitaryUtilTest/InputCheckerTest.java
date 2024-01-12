package com.paymybuddy.unitaryUtilTest;

import com.paymybuddy.dto.BuddyDto;
import com.paymybuddy.dto.PasswordDto;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.service.UserService;
import com.paymybuddy.service.validatorService.InputChecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;


@SpringBootTest
public class InputCheckerTest {
    
    @Autowired
    private InputChecker inputChecker;

    @MockBean
    private UserService userService;
    
    @MockBean
    private BCryptPasswordEncoder passwordEncoderMock;
    
    @Autowired
    private PasswordEncoder passwordEncoder =
            PasswordEncoderFactories.createDelegatingPasswordEncoder();
    
    private final String password = passwordEncoder.encode("test");
    
    private List<User> buddyList = new ArrayList<>();
    private final User
            USER = new User(1, "test@test.com", password, "firstname", "lastname", "USER", new BankAccount(), buddyList, new ArrayList<>());
    private final User USER2 =
            new User(2, "user2test@email.com", password, "firstname2Test", "lastname2Test", "USER", new BankAccount());
    
    private final BuddyDto BUDDYDTO = new BuddyDto("test@test.com");
    private final BuddyDto WRONGBUDDYDTO = new BuddyDto("wrong@test.com");
    @Test
    void shouldSameInputCheckReturnTrueIfTheTwoInputsAreEqualsTest(){
        String firstInput = "test";
        String secondInput = "test";
        Boolean result = inputChecker.sameInputCheck(firstInput, secondInput);
        
        Assertions.assertTrue(result);
    }
    
    @Test
    void shouldSameInputCheckReturnFalseIfTheTwoInputArentEqualsTest(){
        String firstInput = "test";
        String secondInput = "testtest";
        
        Boolean result = inputChecker.sameInputCheck(firstInput, secondInput);
        
        Assertions.assertFalse(result);
    }
    
    @Test
    void shouldSameEmailCheckReturnTrueIfBuddyDtoHaveRightEmailTest(){

        when(userService.getTheAuthenticatedUser()).thenReturn(USER);
        
        Boolean result = inputChecker.sameEmailCheck(BUDDYDTO);
        
        Assertions.assertTrue(result);
    }
    
    @Test
    void shouldSameEmailCheckReturnFalseIfBuddyDtoHaveWrongEmailTest(){
        when(userService.getTheAuthenticatedUser()).thenReturn(USER);
        
        Boolean result = inputChecker.sameEmailCheck(WRONGBUDDYDTO);
        
        Assertions.assertFalse(result);
    }
    
    @Test
    void shouldBuddyEmailExistCheckReturnTrueIfTheUserExistTest(){
        when(userService.getByEmail("test@test.com")).thenReturn(Optional.of(USER));
        
        Boolean result = inputChecker.buddyEmailExistCheck(BUDDYDTO);
        
        Assertions.assertTrue(result);
    }
    
    @Test
    void shouldBuddyEmailExistCheckReturnFalseIfTheUserDoesntExistTest(){
        when(userService.getByEmail("wrong@test.com")).thenReturn(null);
        
        Boolean result = inputChecker.buddyEmailExistCheck(BUDDYDTO);
        
        Assertions.assertFalse(result);
    }
    
    @Test
    void shouldPasswordModifyCheckReturnTrueIfAllInputsAreRightTest(){
        PasswordDto passwordDto = new PasswordDto();
        passwordDto.setOldPassword(password);
        passwordDto.setNewPassword("testtest");
        passwordDto.setNewPasswordCheck("testtest");
        String oldpassword = USER.getPassword();
       
        when(userService.getTheAuthenticatedUser()).thenReturn(USER);
        when(passwordEncoderMock.matches(passwordDto.getOldPassword(), oldpassword)).thenReturn(true);
        
        Boolean result = inputChecker.passwordModifyCheck(passwordDto);
        
        Assertions.assertTrue(result);
    }
    
    @Test
    void shouldPasswordModifyCheckReturnFalseIfTheOldPasswordIsWrongTest(){
        PasswordDto passwordDto = new PasswordDto();
        passwordDto.setOldPassword("$2a$10$wxqw1btUObZAIn8wPRlalOdCYkitnJVlUm.pFAbYHGZMazJeP5rXC");
        passwordDto.setNewPassword("$2a$10$wxqw1btUOaZAIn8wPRlalOdCYkitnJVlUm.pFAbYHGZMazJeP5rXK");
        passwordDto.setNewPasswordCheck("$2a$10$wxqw1btUOaZAIn8wPRlalOdCYkitnJVlUm.pFAbYHGZMazJeP5rXK");
        
        when(userService.getTheAuthenticatedUser()).thenReturn(USER);
        
        Boolean result = inputChecker.passwordModifyCheck(passwordDto);
        
        Assertions.assertFalse(result);
    }
    
    @Test
    void shouldPasswordModifyCheckReturnFalseIfTheNewPasswordIsWrongTest(){
        PasswordDto passwordDto = new PasswordDto();
        passwordDto.setOldPassword("$2a$10$wxqw1btUOaZAIn8wPRlalOdCYkitnJVlUm.pFAbYHGZMazJeP5rXC");
        passwordDto.setNewPassword("$2a$10$wxqw5btUOaZAIn8wPRlalOdCYkitnJVlUm.pFAbYHGZMazJeP5rXK");
        passwordDto.setNewPasswordCheck("$2a$10$wxqw1btUOaZAIn8wPRlalOdCYkitnJVlUm.pFAbYHGZMazJeP5rXK");
        
        when(userService.getTheAuthenticatedUser()).thenReturn(USER);
        
        Boolean result = inputChecker.passwordModifyCheck(passwordDto);
        
        Assertions.assertFalse(result);
    }

    @Test
    void shouldBuddyRelationAlreadyExistReturnTrueWhenUserHaveThisBuddyRelation(){
        BuddyDto buddyDto = new BuddyDto(USER2.getEmail());
        buddyList.add(USER2);
        
        when(userService.getTheAuthenticatedUser()).thenReturn(USER);
        when(userService.getUserByBuddyDto(buddyDto)).thenReturn(USER2);
        
        Boolean result = inputChecker.buddyRelationAlreadyExist(buddyDto);
        
        Assertions.assertTrue(result);
    }
    
    @Test
    void shouldBuddyRelationAlreadyExistReturnFalseWhenUserHaveNotThisBuddyRelation(){
        BuddyDto buddyDto = new BuddyDto("fakeEmail");
        buddyList.add(USER2);
        
        when(userService.getTheAuthenticatedUser()).thenReturn(USER);
        when(userService.getUserByBuddyDto(buddyDto)).thenReturn(null);
        
        Boolean result = inputChecker.buddyRelationAlreadyExist(buddyDto);
        
        Assertions.assertFalse(result);
    }
    
}
