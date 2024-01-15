package com.paymybuddy.service.validatorService;

import com.paymybuddy.dto.BuddyDto;
import com.paymybuddy.dto.PasswordDto;
import com.paymybuddy.model.User;
import com.paymybuddy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class to check the user's inputs.
 * Perform all processing to test the consistency of entries.
 */
@Service
public class InputChecker {
    
    /**
     * Call of SLF4J.
     */
    private final static Logger logger = LoggerFactory.getLogger(UserService.class);
    
    /**
     * Call the UserService to get data from User objects.
     */
    private final UserService userService;
    /**
     * Call the BCryptPasswordEncoder to encode password.
     */
    private final BCryptPasswordEncoder passwordEncoder;
    
    /**
     * The class constructor.
     *
     * @param userService     to get data from User objects.
     * @param passwordEncoder to encode the password.
     */
    public InputChecker(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Method to check if two String are equals.
     * Used to check the dual input of email and password of the register.html.
     * Used to check the dual input of password in modifyPassword form in profile.html.
     *
     * @param firstInput  first String.
     * @param secondInput second String.
     * @return true if the two String are equals and false if aren't.
     */
    public Boolean sameInputCheck(String firstInput, String secondInput) {
        logger.debug("First input: " + firstInput + " second input: " + secondInput);
        return firstInput.equals(secondInput);
    }
    
    /**
     * Method to check if the email of the Principal User is equal to the BuddyDto object parsed.
     * Used in the addBuddy form to check if the user can't try entry his email account to add a buddy.
     *
     * @param buddyDto the object with the data from the form.
     * @return true if the Principal User's email is equals to the buddyDto, and false if isn't.
     */
    public Boolean sameEmailCheck(BuddyDto buddyDto) {
        logger.debug("buddyDto email: " + buddyDto.getEmail());
        return buddyDto.getEmail()
                .equals(userService.getTheAuthenticatedUser()
                        .getEmail());
    }
    
    /**
     * Method to check if the buddy email parsed in the form addBuddy.html is a register User.
     *
     * @param buddyDto the object with the data from the form.
     * @return true if the User exist, and false if isn't.
     */
    public Boolean buddyEmailExistCheck(BuddyDto buddyDto) {
        logger.debug("buddyDto email: " + buddyDto.getEmail());
        return userService.getByEmail(buddyDto.getEmail())
                .isPresent();
    }
    
    /**
     * Method to check if the passwordModify form is correctly completed.
     * Call the sameInputCheck method to check the two new password input.
     * Check if the old password encoded in the PasswordDto object is equals to the Principal User's password attribute.
     *
     * @param passwordDto the object with the data from the form.
     * @return true if all check are right and false if aren't.
     */
    public Boolean passwordModifyCheck(PasswordDto passwordDto) {
        
        String oldPassword = userService.getTheAuthenticatedUser()
                .getPassword();
        
        logger.debug("passwordDto: new password: " + passwordDto.getNewPassword() + " password check input: " +
                passwordDto.getNewPasswordCheck());
        
        return sameInputCheck(passwordDto.getNewPassword(), passwordDto.getNewPasswordCheck()) &&
                passwordEncoder.matches(passwordDto.getOldPassword(), oldPassword);
    }
    
    /**
     * Method to check if the Buddy already exist in the Principal User's buddy attribute.
     *
     * @param buddyDto the object with the data from the form.
     * @return true if the email parsed into the BuddyDto exist in the Principal User's buddy List and false if isn't.
     */
    public Boolean buddyRelationAlreadyExist(BuddyDto buddyDto) {
        
        User authenticatedUser = userService.getTheAuthenticatedUser();
        User buddy = userService.getUserByBuddyDto(buddyDto);
        
        List<User> buddys = authenticatedUser.getBuddys();
        logger.debug("BuddyDto email: " + buddyDto.getEmail() + buddys.toString());
        return buddys.contains(buddy);
    }
}
