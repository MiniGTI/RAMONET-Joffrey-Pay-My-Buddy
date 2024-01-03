package com.paymybuddy.dtoService;

import com.paymybuddy.dto.BuddyDto;
import com.paymybuddy.model.BuddyRelation;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.BuddyRelationRepository;
import com.paymybuddy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The service class for BuddyDto.
 */
@Service
public class BuddyDtoService {
    
    private final static Logger logger = LoggerFactory.getLogger(BuddyDtoService.class);
    
    /**
     * Call of the UserService.
     */
    private final UserService userService;
    
    
    /**
     * Call of the BuddyRelationRepository.
     */
    private final BuddyRelationRepository buddyRelationRepository;
    
    /**
     * BuddyDtoService constructor.
     *
     * @param userService to access at the user service class.
     */
    public BuddyDtoService(UserService userService, BuddyRelationRepository buddyRelationRepository) {
        this.userService = userService;
        this.buddyRelationRepository = buddyRelationRepository;
    }
    
    public Boolean sameEmailCheck(String email, String secondEmail) {
        return (email.equals(secondEmail));
    }
    
    /**
     * Method to check if the email input exist in the User Database.
     *
     * @param email email parsed to identify the User to add in the buddy List of the principal.
     * @return A Boolean.
     */
    public Boolean buddyEmailExistCheck(String email) {
        Optional<User> optUserFind = userService.getByEmail(email);
        
        return optUserFind.isPresent();
    }
    
    /**
     * Method to check if the BuddyRelation is already exist.
     *
     * @param principalId id of the principal user to put in the user_Id column of the user_buddy table.
     * @param buddyId id of the buddy to put in the buddy_Id column of the user_buddy table.
     * @return Boolean.
     */
    public Boolean buddyRelationAlreadyExist(Integer principalId, Integer buddyId) {
        
        Iterable<BuddyRelation> userBuddyRelation = buddyRelationRepository.findBuddyRelationByUserId(principalId);
        
        for(BuddyRelation b : userBuddyRelation) {
            if(b.getBuddy_id()
                    .equals(buddyId)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Method to save a new buddy relation in the link table.
     *
     * @param principal User authenticated.
     * @param buddyDto  The dto object to get the email input from the form.
     */
    
    public BuddyRelation saveNewBuddy(Principal principal, BuddyDto buddyDto) {
        Integer buddyId = null;
        
        Integer userId = userService.getPrincipalId(principal);
        
        Optional<User> optBuddy = userService.getByEmail(buddyDto.getEmail());
        
        if(optBuddy.isPresent()) {
            buddyId = optBuddy.get()
                    .getId();
        } else {
            logger.error("No buddy find.");
        }
        
        BuddyRelation newBuddy = new BuddyRelation(userId, buddyId);
        return buddyRelationRepository.save(newBuddy);
    }
    
    /**
     * Method to create the buddy list of the principal.
     *
     * @param principal User authenticated.
     * @return A list of User.
     */
    
    public List<User> getBuddys(Principal principal) {
        List<User> buddyList = new ArrayList<>();
        
        Integer userId = userService.getPrincipalId(principal);
        
        Iterable<Integer> allBuddyId = userService.getAllBuddyId(userId);
        
        for(Integer id : allBuddyId) {
            Optional<User> buddyFind = userService.getBy(id);
            if(buddyFind.isPresent()) {
                buddyList.add(buddyFind.get());
            } else {
                logger.error("No buddy find.");
            }
        }
        
        return buddyList;
    }
    
    
    /**
     * Method to delete the buddy relation.
     *
     * @param principal User authenticated.
     * @param id        The id of the buddy.
     */
    public void deleteBuddy(Principal principal, Integer id) {
        Integer userId = userService.getPrincipalId(principal);
        buddyRelationRepository.deleteBuddy(userId, id);
    }
}
