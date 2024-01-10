package com.paymybuddy.service;

import com.paymybuddy.dto.BuddyDto;
import com.paymybuddy.dto.PasswordDto;
import com.paymybuddy.dto.UserDto;
import com.paymybuddy.dto.UserModifyDto;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Service class for the User object.
 * Perform all business processing between controllers and the UserRepository.
 */
@Service
public class UserService {
    
    /**
     * Call BCryptPasswordEncoder to encode password.
     */
    private final BCryptPasswordEncoder passwordEncoder;
    
    /**
     * Call the UserRepository to perform CRUDs request to the database.
     */
    private final UserRepository userRepository;
    
    /**
     * The call constructor.
     *
     * @param userRepository  to perform CRUDs request to the database.
     * @param passwordEncoder to encode the password.
     */
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Call the findAll method of the user repository.
     *
     * @return An iterable of all User object present in the user table.
     */
    public Iterable<User> getAll() {
        return userRepository.findAll();
    }
    
    /**
     * Call the findById method of the user repository.
     *
     * @param id id of the User object parsed.
     * @return The User found.
     */
    public Optional<User> getBy(Integer id) {
        return userRepository.findById(id);
    }
    
    /**
     * Call the findByEmail method of the user repository.
     *
     * @param email email of the User parsed.
     * @return The User found.
     */
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Call the deleteBy method of the user repository.
     * Used to delete the account on the profile.html.
     */
    public void deleteBy() {
        User authenticatedUser = getTheAuthenticatedUser();
        userRepository.deleteById(authenticatedUser.getId());
    }
    
    /**
     * Call the save method of the user repository.
     * Used by the register.html form.
     *
     * @param userDto the UserDto created with the register form.
     * @return call the save method of the user repository with attributes of the UserDto parsed.
     * Set the user's email, firstname, lastname of the UserDto parsed
     * Set the user's password of the UserDto parsed after encoded by the BCryptPasswordEncoder.
     * Set the user's role to USER.
     * Create a BankAccount object and give his id to the bankAccount attribute of this new user.
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
     * Method to update the principal's firstname, lastname or/and email with the UserModifyDto object parsed from the profileModify form in the profile.html.
     * Call updateAuthenticatesEmail method to update the Authentication token.
     *
     * @param userModifyDto the object with the data from the form.
     * @return call the save method of the user repository with the modify attributes.
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
            updateAuthenticatedEmail(userModifyDto);
        }
        
        return userRepository.save(user);
    }
    
    /**
     * Method to update the principal's password with the PasswordDto object parsed from the passwordModify form in profile.html.
     *
     * @param passwordDto the object with the data from the form.
     * @return call the save method of the user repository with the new password attribute.
     */
    public User save(PasswordDto passwordDto) {
        User user = getTheAuthenticatedUser();
        
        if(!passwordDto.getNewPassword()
                .isEmpty()) {
            user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        } else {
            throw new RuntimeException("New password not found");
        }
        return userRepository.save(user);
    }
    
    /**
     * Method to save a new buddy relation with the BuddyDto object parsed from the addNewBuddy form of addBuddy.html.
     *
     * @param buddyDto the object with the data from the form.
     */
    public User saveNewBuddy(BuddyDto buddyDto) {
        
        User authenticatedUser = getTheAuthenticatedUser();
        User buddy = getUserByBuddyDto(buddyDto);
        
        List<User> buddys = authenticatedUser.getBuddys();
        buddys.add(buddy);
        authenticatedUser.setBuddys(buddys);
        return userRepository.save(authenticatedUser);
    }
    
    
    /**
     * Method to get the User object of the Principal.
     * Get the Optional<User> and check if is not empty.
     *
     * @return the User object.
     */
    public User getTheAuthenticatedUser() {
        User user = null;
        
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        
        String name = authentication.getName();
        
        Optional<User> optUser = getByEmail(name);
        
        if(optUser.isPresent()) {
            user = optUser.get();
        } else {
            throw new RuntimeException("Problem to get the Principal.");
        }
        return user;
    }
    
    /**
     * Method to get the buddy list of the Principal User.
     *
     * @return a list of User object.
     */
    public List<User> getAllBuddy() {
        User authenticatedUser = getTheAuthenticatedUser();
        return authenticatedUser.getBuddys()
                .stream()
                .toList();
    }
    
    /**
     * Method to get all buddys of the Principal User in a page format.
     *
     * @param numPage  id of the page.
     * @param sizePage the number of User per page.
     * @return a Page.
     */
    public Page<User> getPageBuddyById(int numPage, int sizePage) {
        
        Integer userId = getTheAuthenticatedUser().getId();
        
        Pageable pageable = PageRequest.of(numPage, sizePage);
        
        return userRepository.getPageBuddyById(userId, pageable);
    }
    
    /**
     * Method to delete the buddy relation.
     *
     * @param id The id of the buddy.
     */
    public User deleteBuddy(Integer id) {
        User authenticatedUser = getTheAuthenticatedUser();
        
        Optional<User> optBuddy = getBy(id);
        User buddy = null;
        
        if(optBuddy.isPresent()) {
            buddy = optBuddy.get();
        } else {
            throw new RuntimeException("Buddy not found.");
        }
        
        List<User> buddys = authenticatedUser.getBuddys();
        buddys.remove(buddy);
        return userRepository.save(authenticatedUser);
    }
    
    /**
     * Method to get a User object from a BuddyDto object parsed from the addNewBuddy form of addBuddy.html.
     *
     * @param buddyDto the object with the data from the form.
     * @return A User object.
     */
    public User getUserByBuddyDto(BuddyDto buddyDto) {
        
        User buddy = null;
        Optional<User> optBuddy = getByEmail(buddyDto.getEmail());
        
        if(optBuddy.isPresent()) {
            buddy = optBuddy.get();
        } else {
            throw new RuntimeException("Buddy not found.");
        }
        return buddy;
    }
    
    /**
     * Method to update the Authentication token when the user modify his email.
     * Without the session is closed and the user must sign in.
     *
     * @param userModifyDto the dto to get the new email.
     */
    private void updateAuthenticatedEmail(UserModifyDto userModifyDto) {
        String password = getTheAuthenticatedUser().getPassword();
        
        Collection<SimpleGrantedAuthority> auth =
                (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getAuthorities();
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userModifyDto.getEmail(), password, auth);
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
    }
}
