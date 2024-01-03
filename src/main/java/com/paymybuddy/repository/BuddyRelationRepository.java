package com.paymybuddy.repository;

import com.paymybuddy.model.BuddyRelation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Crud repository interface for the BuddyRelation model.
 */
@Repository
public interface BuddyRelationRepository extends CrudRepository<BuddyRelation, Integer> {
    
    /**
     * Method to execute Sql Query to delete a buddy relation.
     * Delete an entry in the link table user_buddy.
     *
     * @param userId  the principal id.
     * @param buddyId the id of the buddy.
     */
    @Modifying
    @Transactional
    @Query(
            value = "DELETE FROM user_buddy WHERE user_id = :userId AND buddy_id = :buddyId",
            nativeQuery = true)
    void deleteBuddy(
            @Param("userId") Integer userId,
            @Param("buddyId") Integer buddyId);
    
    /**
     * Method to get all BuddyRelation with user_id value, the Integer parsed.
     *
     * @param userId id of the user in the user_id of the link table user_buddy.
     * @return an iterable of all BuddyRelation with user_id value.
     */
    Iterable<BuddyRelation> findBuddyRelationByUserId(Integer userId);
}
