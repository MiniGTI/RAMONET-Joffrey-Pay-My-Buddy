package com.paymybuddy.controller;

import com.paymybuddy.dto.BuddyDto;
import com.paymybuddy.service.UserService;
import com.paymybuddy.service.validatorService.InputChecker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller class for the addBuddy.html.
 * Page to add a new relation between two User.
 * Required an authentication, if no remember-me token present, redirect to the login page.
 */
@Controller
@RequestMapping("/addBuddy")
public class AddBuddyController {
    
    /**
     * Call the UserService to get data from User objects.
     */
    private final UserService userService;
    
    /**
     * Call the InputChecker to perform check methods.
     */
    private final InputChecker inputChecker;
    
    /**
     * The class constructor.
     *
     * @param userService  to get data from User objects.
     * @param inputChecker to perform check methods.
     */
    public AddBuddyController(UserService userService, InputChecker inputChecker) {
        this.userService = userService;
        this.inputChecker = inputChecker;
    }
    
    /**
     * Url to access at the addBuddy page.
     * Page to add a new connexion in the user table.
     *
     * @return the addBuddy.html.
     */
    @GetMapping
    public String addBuddy() {
        return "/html/authenticated/addBuddy";
    }
    
    /**
     * BuddyDto model for the addBuddy form.
     * To add a new relation between two users.
     *
     * @return new buddyDto.
     */
    @ModelAttribute("buddyDto")
    public BuddyDto buddyDto() {
        return new BuddyDto();
    }
    
    /**
     * Add new Buddy form.
     * Call InputChecker to verify:
     * if the email parsed isn't equals to the Principal User email.
     * if the email already exist in the User table.
     * if the buddy relation already exist.
     * Call the saveNewBuddy Userservice method to save a new relation in the buddyList of the Principal User attribute.
     *
     * @param buddyDto The buddyDto object create from the form.
     * @return the url to te contact page.
     * param errorSameEmail if the user try to add a new buddy relation with the Principal User's email.
     * param errorEmail if the email input is not register in the User table.
     * param errorRelation if the buddy relation already exist.
     */
    @PostMapping
    public String addNewBuddy(
            @ModelAttribute("buddyDto") BuddyDto buddyDto) {
        if(inputChecker.sameEmailCheck(buddyDto)) {
            return "redirect:/addBuddy?errorSameEmail";
        }
        
        if(!inputChecker.buddyEmailExistCheck(buddyDto)) {
            return "redirect:/addBuddy?errorEmail";
        }
        
        if(inputChecker.buddyRelationAlreadyExist(buddyDto)) {
            return "redirect:/addBuddy?errorRelation";
        }
        
        userService.saveNewBuddy(buddyDto);
        return "redirect:/contact";
    }
}
