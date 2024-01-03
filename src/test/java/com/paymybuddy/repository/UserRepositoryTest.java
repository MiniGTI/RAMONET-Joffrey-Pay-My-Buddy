package com.paymybuddy.repository;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.BuddyRelation;
import com.paymybuddy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;
    
    private final User USER = new User("savetest@gmail.com", "passTest", "first", "last", "USER", new BankAccount());
    
    @Test
    void userSaveTest() {
        User savedUser = userRepository.save(USER);
        Assertions.assertEquals(savedUser, entityManager.find(User.class, savedUser.getId()));
    }
    
    @Test
    void userUpdateTest() {
        entityManager.persist(USER);
        String newPassword = "newPassTest";
        USER.setPassword(newPassword);
        
        userRepository.save(USER);
        
        Assertions.assertEquals(newPassword, entityManager.find(User.class, USER.getId())
                .getPassword());
    }
    
    @Test
    void userFindByIdTest() {
        entityManager.persist(USER);
        
        Optional<User> findUser = userRepository.findById(USER.getId());
        
        Assertions.assertEquals(USER.getId(), findUser.get()
                .getId());
    }
    
    @Test
    void userFindByEmailTest() {
        entityManager.persist(USER);
        
        Optional<User> findUser = userRepository.findByEmail(USER.getEmail());
        
        Assertions.assertEquals(USER.getId(), findUser.get()
                .getId());
    }
    
    @Test
    void userFindAllTest() {
        User user2 = new User("savetest2@gmail.com", "passTest", "first", "last", "USER", new BankAccount());
        entityManager.persist(USER);
        entityManager.persist(user2);
        
        Iterable<User> findUsers = userRepository.findAll();
        
        assertThat(findUsers).contains(USER, user2);
    }
    
    @Test
    void userDeleteByIdTest() {
        entityManager.persist(USER);
        
        userRepository.deleteById(USER.getId());
        
        Assertions.assertNull(entityManager.find(User.class, USER.getId()));
    }
    
    @Test
    void userGetAllBuddyIdTest() {
        BuddyRelation buddyRelation = new BuddyRelation(1, 2);
        entityManager.persist(buddyRelation);
        
        Iterable<Integer> result = userRepository.getAllBuddyId(1);
        
        assertThat(result).contains(buddyRelation.getBuddy_id());
    }
}
