package com.paymybuddy.controller;

import com.paymybuddy.model.User;
import com.paymybuddy.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Controller class for the contact.html.
 * Page to add a new relation between two User.
 * Page to view the Principal User's buddyList.
 * Required an authentication, if no remember-me token present, redirect to the login page.
 */
@Controller
@RequestMapping("/contact")
public class ContactController {
    
    /**
     * Call the UserService to get data from User objects.
     */
    private final UserService userService;
    
    /**
     * The class constructor.
     *
     * @param userService to get data from User objects.
     */
    public ContactController(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * Add the Principal User's BuddyList into a Page format.
     * The pageSize manage the number of buddy per page.
     * Call the getPageBuddyById UserService to get the buddyList by page of 3 buddy.
     *
     * @param model to parse the buddyList data to the view.
     * @param page  the pagination configuration.
     * @return contact.html
     */
    @GetMapping()
    public String buddyList(Model model,
                            @RequestParam(defaultValue = "0") Integer page) {
        int pageSize = 3;
        
        Page<User> buddyPage = userService.getPageBuddyById(page, pageSize);
        
        List<User> buddys = buddyPage.getContent();
        
        model.addAttribute("buddys", buddys);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", buddyPage.getTotalPages());
        model.addAttribute("totalItem", buddyPage.getTotalElements());
        return "/html/authenticated/contact";
    }
    
    /**
     * The delete button.
     * To delete the buddy relation linked.
     * Call the deleteBuddy UserService to remove the Buddy in the Principal User buddyList attribute.
     *
     * @param buddyId id of the buddy.
     * @return contact.html.
     */
    @PostMapping()
    public String deleteBuddy(
            @RequestParam Integer buddyId) {
        userService.deleteBuddy(buddyId);
        return "redirect:/contact";
    }
}
