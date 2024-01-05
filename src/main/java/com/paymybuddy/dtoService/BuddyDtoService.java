package com.paymybuddy.dtoService;

import com.paymybuddy.dto.BuddyDto;
import com.paymybuddy.model.User;
import com.paymybuddy.service.UserService;
import org.springframework.stereotype.Service;

/**
 * The service class for BuddyDto.
 */
@Service
public class BuddyDtoService {

    /**
     * Call of the UserService.
     */
    private final UserService userService;

    /**
     * BuddyDtoService constructor.
     *
     * @param userService to access at the user service class.
     */
    public BuddyDtoService(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * Method to check if the two password input have the same entry.
     *
     * @param buddyDto the buddyDto create into the form.
     * @return Boolean.
     */
    public Boolean sameEmailCheck(BuddyDto buddyDto) {
        
        User authenticatedUser = userService.getTheAuthenticatedUser();
        
        return (buddyDto.getEmail().equals(authenticatedUser.getEmail()));
    }
  

}
