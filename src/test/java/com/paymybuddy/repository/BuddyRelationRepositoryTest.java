package com.paymybuddy.repository;

import com.paymybuddy.model.BuddyRelation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BuddyRelationRepositoryTest {
    
    @Autowired
    private BuddyRelationRepository buddyRelationRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    private final BuddyRelation buddyRelation = new BuddyRelation(1, 2);
    
    @Test
    void buddyRelationSaveTest() {
        BuddyRelation result = buddyRelationRepository.save(buddyRelation);
        
        Assertions.assertEquals(result, entityManager.find(BuddyRelation.class, result.getId()));
    }
    
    @Test
    void buddyRelationFindByUserIdTest() {
        entityManager.persist(buddyRelation);
        
        Iterable<BuddyRelation> result = buddyRelationRepository.findBuddyRelationByUserId(1);
        
        assertThat(result).contains(buddyRelation);
    }
    
    @Test
    @Disabled
    void buddyRelationDeleteTest() {
        
        BuddyRelation buddyRelation = new BuddyRelation(1, 2);
        
        entityManager.persist(buddyRelation);
        
        System.out.println(buddyRelation.getId());
        System.out.println(buddyRelationRepository.findAll());
        buddyRelationRepository.deleteBuddy(1, 2);
        System.out.println(buddyRelationRepository.findAll());
        
        Assertions.assertNull(entityManager.find(BuddyRelation.class, buddyRelation.getId()));
    }
}