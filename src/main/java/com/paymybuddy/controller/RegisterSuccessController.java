package com.paymybuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller class for the registerSuccess.html.
 * Page to call register is success to the new User.
 */
@Controller
@RequestMapping("/registerSuccess")
public class RegisterSuccessController {
    
    /**
     * Url to access at the registerSuccess page.
     * Page to call the new user to sign in.
     *
     * @return the registerSuccess.html.
     */
    @GetMapping
    public String registrationSuccess() {
        return "/html/registerSuccess";
    }
}
