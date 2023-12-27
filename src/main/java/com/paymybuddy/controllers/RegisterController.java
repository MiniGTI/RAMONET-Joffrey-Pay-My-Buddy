package com.paymybuddy.controllers;

import com.paymybuddy.dto.UserDto;
import com.paymybuddy.dto.UserDtoService;
import com.paymybuddy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for the register.html.
 */
@Controller
@RequestMapping("/register")
public class RegisterController {
    
    /**
     * Call the UserService.
     */
    private final UserService userService;
    
    /**
     * Call the UserDtoService.
     */
    private final UserDtoService userDtoService;
    
    /**
     * UserService constructor.
     *
     * @param userService    to access at the user service class.
     * @param userDtoService to access at the userDto service class.
     */
    public RegisterController(UserService userService, UserDtoService userDtoService) {
        this.userService = userService;
        this.userDtoService = userDtoService;
    }
    
    /**
     * Url to access at the register page.
     *
     * @return register.html.
     * Page to register a new user in the Database's user table.
     */
    @GetMapping
    public String register() {
        return "html/register";
    }
    
    /**
     * UserDto model for the register form.
     *
     * @return new userDto.
     * Required an authentication, if no remember-me token present, redirect to the login page.
     */
    @ModelAttribute("userDto")
    public UserDto userDto() {
        return new UserDto();
    }
    
    /**
     * Register form.
     *
     * @param userDto The userDto object required to creat a new User object.
     * @return the Email error message if the two email input are not equals.
     * the Password error message if the two password input are not equals.
     * the registerSuccess.html if the new userDto object is created.
     */
    @PostMapping
    public String registrationUser(
            @ModelAttribute("userDto") UserDto userDto) {
        
        if(!userDtoService.emailCheck(userDto.getEmail(), userDto.getEmailCheck())) {
            return "redirect:/register?errorEmail";
        }
        
        if(!userDtoService.passwordCheck(userDto.getPassword(), userDto.getPasswordCheck())) {
            return "redirect:/register?errorPassword";
        }
        userService.save(userDto);
        return "redirect:/registerSuccess";
    }
}
