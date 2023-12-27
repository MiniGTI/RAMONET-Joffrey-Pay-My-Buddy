package com.paymybuddy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registerSuccess")
public class RegisterSuccessController {


    @GetMapping
    public String registrationSuccess(){
        return "/html/registerSuccess";
    }
}
