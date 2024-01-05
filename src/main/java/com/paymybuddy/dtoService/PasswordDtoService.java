package com.paymybuddy.dtoService;

import com.paymybuddy.dto.PasswordDto;
import com.paymybuddy.model.User;
import com.paymybuddy.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The service class for the passwordDto.
 */
@Service
public class PasswordDtoService {
    
    /**
     * To call the UserService;
     */
    private final UserService userService;
    /**
     * Constructor of PasswordDtoService.
     *
     * @param userService to access at the user service class.
     */
    public PasswordDtoService(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * Method to check if:
     *      The old password is equals to the principal's actual password.
     *      The new password is equals to the new password check input.
     *
     * @param passwordDto dto to parse the data from the form.
     * @return a boolean.
     */
    public Boolean passwordModifyCheck(PasswordDto passwordDto) {
        User user = null;
        
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        
        String name = authentication.getName();
        
        Optional<User> optUser = userService.getByEmail(name);
        
        if(optUser.isPresent()) {
            user = optUser.get();
        } else {
            throw new RuntimeException("no user find");
        }
        
        String oldPassword = user.getPassword();
        
        return oldPassword.equals(passwordDto.getOldPassword()) && passwordDto.getNewPassword()
                .equals(passwordDto.getNewPasswordCheck());
    }
}
