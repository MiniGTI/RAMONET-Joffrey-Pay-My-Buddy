package com.paymybuddy.dto;

import org.springframework.stereotype.Service;

/**
 * The service class for UserDto.
 */
@Service
public class UserDtoService {
    
    /**
     * Methode to check if the confirmation input of password in the register form is equal to the first password input.
     * @param password first password input in the register form. Attribute of a UserDto object.
     * @param passwordCheck second password input in the register form. Attribute of a UserDto object.
     * @return a Boolean.
     */
    public Boolean passwordCheck(String password, String passwordCheck){
        return password.equals(passwordCheck);
    }
    
    /**
     * Methode to check if the confirmation input of email in the register form is equal to the first email input.
     * @param email first email input in the register form. Attribute of a UserDto object.
     * @param emailCheck second email input in the register form. Attribute of a UserDto object.
     * @return a Boolean.
     */
    public Boolean emailCheck(String email, String emailCheck){
        return email.equals(emailCheck);
    }
}
