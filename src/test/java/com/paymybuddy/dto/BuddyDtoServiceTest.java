package com.paymybuddy.dto;

import com.paymybuddy.dtoService.BuddyDtoService;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.BuddyRelation;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.BuddyRelationRepository;
import com.paymybuddy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BuddyDtoServiceTest {
    
    private static BuddyDtoService buddyDtoService;
    
    @MockBean
    private UserService userService;
    
    @MockBean
    private BuddyRelationRepository buddyRelationRepository;
    
    @Mock
    private Principal principal;
    
    @BeforeEach
    public void SetUp() {
        buddyDtoService = new BuddyDtoService(userService, buddyRelationRepository);
    }
    
    private final User user1 =
            new User(1, "usertest@email.com", "$2a$10$8YuJdmjucTcYUnargyHj8u64QuxmQGTNB7cpAIBSw2wYonwyOzDK6",
                    "firstnameTest", "lastnameTest", "USER", new BankAccount());
    
    private final User user2 =
            new User(2, "usertest2@email.com", "$2a$10$8YuJdmjucTcYUnargyHj1u64QuxmQGTNB7cpAIBSw2wYonwyOzDK6",
                    "firstname2Test", "lastname2Test", "USER", new BankAccount());
    private final String EMAIL = "usertest@email.com";
    private final Iterable<BuddyRelation> buddyRelationsList =
            Arrays.asList(new BuddyRelation(1, 2), new BuddyRelation(1, 5));
    private final BuddyDto buddyDto = new BuddyDto("usertest2@email.com");
    
    
    @Test
    void sameEmailCheckIfTrue() {
        Boolean result = buddyDtoService.sameEmailCheck(user1.getEmail(), user1.getEmail());
        
        Assertions.assertTrue(result);
    }
    
    @Test
    void sameEmailCheckIfFalse() {
        Boolean result = buddyDtoService.sameEmailCheck(user1.getEmail(), user2.getEmail());
        
        Assertions.assertFalse(result);
    }
    
    @Test
    void buddyEmailExistCheckWhenUserExist() {
        when(userService.getByEmail(EMAIL)).thenReturn(Optional.of(user1));
        
        Boolean buddy = buddyDtoService.buddyEmailExistCheck(EMAIL);
        
        Assertions.assertTrue(buddy);
    }
    
    @Test
    void buddyEmailExistCheckWhenNoUserFind() {
        Boolean buddy = buddyDtoService.buddyEmailExistCheck("");
        
        Assertions.assertFalse(buddy);
    }
    
    @Test
    void buddyRelationAlreadyExistWhenItsTrue() {
        Integer principalId = 1;
        when(buddyRelationRepository.findBuddyRelationByUserId(principalId)).thenReturn(buddyRelationsList);
        
        Boolean result = buddyDtoService.buddyRelationAlreadyExist(principalId, 2);
        
        Assertions.assertTrue(result);
    }
    
    @Test
    void buddyRelationAlreadyExistWhenItsFalse() {
        Integer principalId = 1;
        when(buddyRelationRepository.findBuddyRelationByUserId(principalId)).thenReturn(buddyRelationsList);
        
        Boolean result = buddyDtoService.buddyRelationAlreadyExist(principalId, 3);
        
        Assertions.assertFalse(result);
    }
    
    @Test
    @Disabled
    void saveNewBuddyTest() {
        when(principal.getName()).thenReturn("usertest@email.com");
        when(userService.getPrincipalId(principal)).thenReturn(1);
        when(userService.getByEmail(buddyDto.getEmail())).thenReturn(Optional.of(user2));
        
        BuddyRelation result = buddyDtoService.saveNewBuddy(principal, buddyDto);
        BuddyRelation resultExpected = new BuddyRelation(1, 2);
        
        Assertions.assertEquals(resultExpected, result);
    }
    
    @Test
    void getBuddysTest() {
        List<Integer> allBuddyId = new ArrayList<>(List.of(user2.getId()));
        
        when(userService.getPrincipalId(principal)).thenReturn(1);
        when(userService.getAllBuddyId(1)).thenReturn(allBuddyId);
        when(userService.getBy(2)).thenReturn(Optional.of(user2));
        
        List<User> result = buddyDtoService.getBuddys(principal);
        List<User> resultExpected = new ArrayList<>(List.of(user2));
        
        Assertions.assertEquals(resultExpected.size(), result.size());
    }
    
    
    @Test
    void deleteBuddyRelation() {
        when(userService.getPrincipalId(principal)).thenReturn(1);
        doNothing().when(buddyRelationRepository)
                .deleteBuddy(1, 2);
        buddyDtoService.deleteBuddy(principal, 2);
        
        Assertions.assertDoesNotThrow(() -> buddyDtoService.deleteBuddy(principal, 2));
    }
}
