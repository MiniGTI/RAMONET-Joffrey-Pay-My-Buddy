package com.paymybuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * Controller class for the registerSuccess.html.
 */
@Controller
@RequestMapping("/registerSuccess")
public class RegisterSuccessController {
    
    /**
     * Url to access at the registerSuccess page.
     *
     * @return the registerSuccess.html.
     * Page to call the new user to sign in.
     */
    @GetMapping
    public String registrationSuccess(){
        return "/html/registerSuccess";
    }
}
