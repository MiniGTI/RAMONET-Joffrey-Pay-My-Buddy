package com.paymybuddy.repository;

import com.paymybuddy.model.User;
import org.springframework.data.repository.CrudRepository;
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
    public Optional<User> findByEmail(String email);
}
