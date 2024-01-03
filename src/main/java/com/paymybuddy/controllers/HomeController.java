package com.paymybuddy.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Controller class for the home.html.
 */
@Controller
@RequestMapping("/home")
public class HomeController {
    
    /**
     * Url to access at the home page.
     *
     * @return the home.html.
     * First page after authentication.
     */
    @GetMapping
    public String home() {
        return "/html/authenticated/home";
    }
}
