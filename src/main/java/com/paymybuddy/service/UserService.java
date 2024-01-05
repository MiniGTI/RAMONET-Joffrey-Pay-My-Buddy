package com.paymybuddy.service;

import com.paymybuddy.dto.BuddyDto;
import com.paymybuddy.dto.PasswordDto;
import com.paymybuddy.dto.UserDto;
import com.paymybuddy.dto.UserModifyDto;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
     * Method to update the principal's firstname, lastname, email with the userModifyDto parsed.
     *
     * @param userModifyDto the object with the data from the form.
     * @return user.
     */
    
    public User save(UserModifyDto userModifyDto) {
        User user = getTheAuthenticatedUser();
        
        if(!userModifyDto.getFirstName()
                .isEmpty()) {
            user.setFirstname(userModifyDto.getFirstName());
        }
        if(!userModifyDto.getLastname()
                .isEmpty()) {
            user.setLastname(userModifyDto.getLastname());
        }
        if(!userModifyDto.getEmail()
                .isEmpty()) {
            user.setEmail(userModifyDto.getEmail());
        }
        System.out.println(userModifyDto);
        return userRepository.save(user);
    }
    
    /**
     * Method to update the principal's password with the passwordDto parsed .
     *
     * @param passwordDto the object with the data from the form.
     * @return user.
     */
    public User save(PasswordDto passwordDto) {
        User user = getTheAuthenticatedUser();
        
        if(!passwordDto.getNewPassword()
                .isEmpty()) {
            user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        }
        return userRepository.save(user);
    }
    
    public User saveNewBuddy(BuddyDto buddyDto){
        
        User authenticatedUser = getTheAuthenticatedUser();
        User buddy = getUserByBuddyDto(buddyDto);
        
        Set<User> buddys = authenticatedUser.getBuddys();
        buddys.add(buddy);
        authenticatedUser.setBuddys(buddys);
        
        return userRepository.save(authenticatedUser);
    }
    
    /**
     * Method to check if the email input exist in the User Database.
     *
     * @return A Boolean.
     */
    public Boolean buddyEmailExistCheck(BuddyDto buddyDto) {
        
        Optional<User> optUserFind = getByEmail(buddyDto.getEmail());
        
        return optUserFind.isPresent();
    }
    
    public Boolean buddyRelationAlreadyExist(BuddyDto buddyDto){
        
        User authenticatedUser = getTheAuthenticatedUser();
        User buddy = getUserByBuddyDto(buddyDto);
        
        Set<User> buddys = authenticatedUser.getBuddys();
        
        return buddys.contains(buddy);
    }
    
    public User getTheAuthenticatedUser(){
        User user = null;
        
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        
        String name = authentication.getName();
        
        Optional<User> optUser = getByEmail(name);
        
        if(optUser.isPresent()){
            user = optUser.get();
        } else {
            throw new RuntimeException("Problem to get the Principal.");
        }
        return user;
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
     * @return a list of Integer.
     */
    public List<User> getAllBuddyId() {
        User authenticatedUser = getTheAuthenticatedUser();
        return authenticatedUser.getBuddys().stream().toList();
    }
    
    public Page<User> getPageBuddyById(int numPage, int sizePage) {
        

        User authenticatedUser = getTheAuthenticatedUser();
        
        Integer userId = authenticatedUser
                .getId();
        
        Pageable pageable = PageRequest.of(numPage, sizePage);
        
        return userRepository.getPageBuddyById(userId, pageable);
    }
    
    
    /**
     * Method to delete the buddy relation.
     *
      * @param id The id of the buddy.
     */
    public void deleteBuddy(Integer id) {
        User authenticatedUser = getTheAuthenticatedUser();
        
        Optional<User> optBuddy = getBy(id);
        User buddy = null;
        
        if(optBuddy.isPresent()){
            buddy = optBuddy.get();
        }
        
        Set<User> buddys = authenticatedUser.getBuddys();
        buddys.remove(buddy);
        userRepository.save(authenticatedUser);
    }
    
    private User getUserByBuddyDto(BuddyDto buddyDto){
        
        User buddy = null;
        Optional<User> optBuddy = getByEmail(buddyDto.getEmail());
        
        if(optBuddy.isPresent()) {
            buddy = optBuddy.get();
        } else {
            throw new RuntimeException("Buddy not found.");
        }
        return buddy;
    }
    
}
