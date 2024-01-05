package com.paymybuddy.repository;

import com.paymybuddy.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Crud repository interface for the User model.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    
    /**
     * Method to find a User in the Database with the email attribute.
     *
     * @param email attribute of the User object parsed.
     * @return the User find.
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Method to get all buddy's id from the link table user_buddy.
     *
     * @param userId id of the principal.
     * @return An iterable of Integer, all buddy's id.
     */
    @Query(
            value = "SELECT buddy_id FROM user_buddy WHERE user_id = :userId",
            nativeQuery = true)
    Iterable<Integer> getAllBuddyId(
            @Param("userId") Integer userId);

}
