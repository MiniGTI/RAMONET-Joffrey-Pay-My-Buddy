package com.paymybuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller class for the Login.html.
 * Page to authenticate User with email and password.
 * Page to access at the register.html to register a new User.
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    /**
     * Url to access at the login page.
     *
     * @return login.html.
     */
    @GetMapping
    public String login() {
        return "html/login";
    }
    
    /**
     * Url call to logout.
     *
     * @return login.html with param logout.
     */
    @GetMapping("logout")
    public String logout() {
        return "html/login";
    }
}