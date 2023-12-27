package com.paymybuddy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller class for the Login.html.
 */
@Controller
public class LoginController {
    /**
     * Url to access at the home page.
     *
     * @return home.html.
     * Required an authentication, if no remember-me token present, redirect to the login page.
     */
    @GetMapping("/")
    public String home() {
        return "html/authenticated/home";
    }
    
    /**
     * Url to access at the login page.
     *
     * @return login.html.
     */
    @GetMapping("/login")
    public String login() {
        return "html/login";
    }
    
    /**
     * Url call to logout.
     *
     * @return login.html with param logout.
     */
    @GetMapping("/login.logout")
    public String logout() {
        return "html/login";
    }
}