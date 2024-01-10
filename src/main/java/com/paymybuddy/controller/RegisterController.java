package com.paymybuddy.controller;

import com.paymybuddy.dto.UserDto;
import com.paymybuddy.service.UserService;
import com.paymybuddy.util.InputChecker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller class for the register.html.
 * Page to register a new User.
 */
@Controller
@RequestMapping("/register")
public class RegisterController {
    
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
    public RegisterController(UserService userService, InputChecker inputChecker) {
        this.userService = userService;
        this.inputChecker = inputChecker;
    }
    
    /**
     * Url to access at the register page.
     * Page to add a new User in the User table.
     *
     * @return register.html.
     */
    @GetMapping
    public String register() {
        return "html/register";
    }
    
    /**
     * UserDto model for the register form.
     * To add a new User.
     *
     * @return new userDto.
     */
    @ModelAttribute("userDto")
    public UserDto userDto() {
        return new UserDto();
    }
    
    /**
     * Register form.
     * Call sameInputCheck InputChecker method to verify:
     * if the two inputs email are equals.
     * if the two inputs password are equals.
     * Call the save UserService method to create the new User and save it in the User table.
     *
     * @param userDto The userDto object required to create a new User object.
     * @return the url to the registerSuccess page or:
     * register?errorEmail if the two email inputs are not equals.
     * register?errorPassword if the two password inputs are not equals.
     */
    @PostMapping
    public String registrationUser(
            @ModelAttribute("userDto") UserDto userDto) {
        
        if(!inputChecker.sameInputCheck(userDto.getEmail(), userDto.getEmailCheck())) {
            return "redirect:/register?errorEmail";
        }
        
        if(!inputChecker.sameInputCheck(userDto.getPassword(), userDto.getPasswordCheck())) {
            return "redirect:/register?errorPassword";
        }
        userService.save(userDto);
        return "redirect:/registerSuccess";
    }
}
