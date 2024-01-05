package com.paymybuddy.controller;

import com.paymybuddy.dtoService.BuddyDtoService;
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
 */
@Controller
@RequestMapping("/contact")
public class ContactController {
    
    /**
     * Call BuddyDtoService.
     */
    private final BuddyDtoService buddyDtoService;
    private final UserService userService;
    
    /**
     * ContactController constructor.
     */
    public ContactController(BuddyDtoService buddyDtoService, UserService userService) {
        this.buddyDtoService = buddyDtoService;
        this.userService = userService;
    }
    
    /**
     * Add the BuddyList of the authenticated User.
     *
     * @param model     to parse the buddyList data to the view.
     * @param page      the configuration of the pagination.
     * @return contact.html
     */
    @GetMapping()
    public String buddyList(Model model,
                            @RequestParam(defaultValue = "0") Integer page) {
        Integer pageSize = 3;
        
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
     *
     * @param buddyId   id of the buddy.
     * @return contact.html.
     */
    @PostMapping()
    public String deleteBuddy(@RequestParam Integer buddyId) {
        userService.deleteBuddy(buddyId);
        return "redirect:/contact";
    }
}
