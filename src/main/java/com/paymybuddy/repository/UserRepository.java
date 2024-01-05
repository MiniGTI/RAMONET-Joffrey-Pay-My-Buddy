package com.paymybuddy.repository;

import com.paymybuddy.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    
    @Query(value = "SELECT * FROM user JOIN user_buddys ON (user.id = user_buddys.buddys_id) WHERE user_buddys.user_id = :id", nativeQuery = true)
    Page<User> getPageBuddyById(@Param("id") Integer id, Pageable pageable);
    
    
}
