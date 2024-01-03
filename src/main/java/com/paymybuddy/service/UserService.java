package com.paymybuddy.service;

import com.paymybuddy.dto.UserDto;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

/**
 * Service class for the User object.
 */
@Service
public class UserService {
    
    private final static Logger logger = LoggerFactory.getLogger(UserService.class);
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
    
    
    /**
     * Method to get the Id of the principal
     *
     * @param principal User authenticated.
     * @return An Integer, the id of the principal.
     */
    public Integer getPrincipalId(Principal principal) {
        Integer userId = null;
        Optional<User> optUser = userRepository.findByEmail(principal.getName());
        
        if(optUser.isPresent()) {
            userId = optUser.get()
                    .getId();
        } else {
            logger.error("No principal find.");
        }
        
        return userId;
    }
    
    /**
     * Method to get a buddy's id into a list of user's buddys.
     *
     * @param userId id of the user.
     * @return a list of Integer.
     */
    public Iterable<Integer> getAllBuddyId(Integer userId) {
        return userRepository.getAllBuddyId(userId);
    }
}
