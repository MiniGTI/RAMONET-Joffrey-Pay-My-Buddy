package com.paymybuddy.controller;

import com.paymybuddy.dto.BuddyDto;
import com.paymybuddy.dtoService.BuddyDtoService;
import com.paymybuddy.model.User;
import com.paymybuddy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

/**
 * Controller class for the addBuddy.html.
 */
@Controller
@RequestMapping("/addBuddy")
public class AddBuddyController {
    
    /**
     * Call the UserService.
     */
    private final UserService userService;
    
    /**
     * Call the BuddyDtoService.
     */
    private final BuddyDtoService buddyDtoService;
    
    /**
     * AddBuddyController constructor
     *
     * @param userService     to access at the user service class.
     * @param buddyDtoService to access at the buddyDto service class.
     */
    public AddBuddyController(UserService userService, BuddyDtoService buddyDtoService) {
        this.userService = userService;
        this.buddyDtoService = buddyDtoService;
    }
    
    /**
     * Url to access at the addBuddy page.
     *
     * @return the addBuddy.html.
     * Page to add a new connexion in the user table.
     */
    @GetMapping
    public String addBuddy() {
        return "/html/authenticated/addBuddy";
    }
    
    /**
     * BuddyDto model for the form.
     *
     * @return new buddyDto.
     */
    @ModelAttribute("buddyDto")
    public BuddyDto buddyDto() {
        return new BuddyDto();
    }
    
    /**
     * Add new Buddy form.
     *
     * @param buddyDto  The buddyDto object required to add the new connexion.
     * @param principal The User actually authenticated.
     * @return the url to te contact page.
     */
    @PostMapping
    public String addNewBuddy(
            @ModelAttribute("buddyDto") BuddyDto buddyDto, Principal principal) {
        if(buddyDtoService.sameEmailCheck(buddyDto.getEmail(), principal.getName())) {
            return "redirect:/addBuddy?errorSameEmail";
        }
        
        if(!buddyDtoService.buddyEmailExistCheck(buddyDto.getEmail())) {
            return "redirect:/addBuddy?errorEmail";
        }
        
        Integer principalId = userService.getPrincipalId(principal);
        Integer buddyId = 0;
        Optional<User> optBuddy = userService.getByEmail(buddyDto.getEmail());
        if(optBuddy.isPresent()) {
            buddyId = optBuddy.get()
                    .getId();
        }
        
        if(buddyDtoService.buddyRelationAlreadyExist(principalId, buddyId)) {
            return "redirect:/addBuddy?errorRelation";
        }
        
        buddyDtoService.saveNewBuddy(principal, buddyDto);
        return "redirect:/contact";
    }
}
