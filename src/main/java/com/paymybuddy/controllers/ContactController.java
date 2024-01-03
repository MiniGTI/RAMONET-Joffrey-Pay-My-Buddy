package com.paymybuddy.controllers;

import com.paymybuddy.dtoService.BuddyDtoService;
import com.paymybuddy.model.User;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

/**
 * Controller class for the contact.html.
 */
@Controller
@RequestMapping("/contact")
public class ContactController {
    
    
    private final BuddyDtoService buddyDtoService;
    
    /**
     * ContactController constructor.
     */
    public ContactController(BuddyDtoService buddyDtoService) {
        this.buddyDtoService = buddyDtoService;
    }
    
    /**
     * Add the BuddyList of the authenticated User.
     *
     * @param principal authenticated User.
     * @param model     to parse the buddyList data to the view.
     * @param page      the configuration of the pagination.
     * @return contact.html
     */
    @GetMapping()
    public String buddyList(Principal principal, Model model,
                            @RequestParam(defaultValue = "0") Integer page) {
        List<User> buddys = buddyDtoService.getBuddys(principal);
        
        Pageable pageable = PageRequest.of(page, 2, Sort.unsorted());
        
        Page<User> buddyList = new PageImpl<>(buddys, pageable, buddys.size());
        model.addAttribute("buddyList", buddyList);
        model.addAttribute("currentPage", pageable);
        return "/html/authenticated/contact";
    }
    
    /**
     * The delete button.
     * To delete the buddy relation linked.
     *
     * @param principal User authenticated.
     * @param buddyId   id of the buddy.
     * @return contact.html.
     */
    @PostMapping()
    public String deleteBuddy(Principal principal,
                              @RequestParam Integer buddyId) {
        buddyDtoService.deleteBuddy(principal, buddyId);
        return "redirect:/contact";
    }
}
