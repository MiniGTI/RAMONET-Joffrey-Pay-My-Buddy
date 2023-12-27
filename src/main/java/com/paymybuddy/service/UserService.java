package com.paymybuddy.service;

import com.paymybuddy.dto.UserDto;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for the User object.
 */
@Service
public class UserService {
    
    /**
     * Call BCryptPasswordEncoder.class to encode password.
     */
    private final BCryptPasswordEncoder passwordEncoder;
    
    /**
     * Call the UserRepository.
     */
    private final UserRepository userRepository;
    
    /**
     * UserService constructor.
     *
     * @param userRepository  to access at the table of user of the Database.
     * @param passwordEncoder to encode the password
     */
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Call the findAll method of the user repository.
     *
     * @return An iterable of all User object present in the Database's user table.
     */
    public Iterable<User> getAll() {
        return userRepository.findAll();
    }
    
    /**
     * Call the findById method of the user repository.
     *
     * @param id id of the User parsed.
     * @return The User object with the id parsed.
     */
    public Optional<User> getBy(Integer id) {
        return userRepository.findById(id);
    }
    
    /**
     * Call the findByEmail method of the user repository.
     *
     * @param email email of the User parsed.
     * @return The User object with the email parsed.
     */
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Call the deleteBy method of the user repository.
     *
     * @param id id of the User parsed.
     */
    public void deleteBy(Integer id) {
        userRepository.deleteById(id);
    }
    
    /**
     * Call the save method of the user repository.
     *
     * @param userDto the UserDto created with the register form.
     * @return call save method of the user repository with attributes of the UserDto parsed.
     * Set the user's email, firstname, lastname of the UserDto parsed
     * Set the user's password of the UserDto parsed after encoded by the BCryptPasswordEncoder.
     * Set the user's role to USER.
     * Create a BankAccount and give his id to this user.
     */
    public User save(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole("USER");
        user.setBankAccount(new BankAccount());
        
        return userRepository.save(user);
    }
}
