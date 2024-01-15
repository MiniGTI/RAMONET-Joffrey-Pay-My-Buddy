package com.paymybuddy.controller;

import com.paymybuddy.dto.PasswordDto;
import com.paymybuddy.dto.UserModifyDto;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.service.TransactionService;
import com.paymybuddy.service.UserService;
import com.paymybuddy.service.validatorService.InputChecker;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

/**
 * Controller class for the profile.html.
 * Page to update the Principal User personals information.
 * Page to update the Principal User password.
 * Page to delete the Principal User account.
 * Page to see the last Principal User's BankAccount transaction and the IBAN, SWIFT, BALANCE.
 * Required an authentication, if no remember-me token present, redirect to the login page.
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {
    
    /**
     * Call the UserService to get data from User objects.
     */
    private final UserService userService;
    
    /**
     * Call the InputChecker to perform check methods.
     */
    private final InputChecker inputChecker;
    
    /**
     * Call the TransactionService to get data from Transaction objects.
     */
    private final TransactionService transactionService;
    
    
    /**
     * The class constructor.
     *
     * @param userService        to get data from User objects.
     * @param inputChecker       to perform check methods.
     * @param transactionService to get data from Transaction objects.
     */
    public ProfileController(UserService userService, InputChecker inputChecker,
                             TransactionService transactionService) {
        this.userService = userService;
        this.inputChecker = inputChecker;
        this.transactionService = transactionService;
    }
    
    /**
     * Url to acces at the profile page.
     * To manage all data fields of the page.
     *
     * @param model to parse data to the view.
     * @return profile.html
     */
    @GetMapping
    public String profile(Model model) {
        
        User authenticatedUser = userService.getTheAuthenticatedUser();
        
        String firstName = authenticatedUser.getFirstname();
        String lastName = authenticatedUser.getLastname();
        String email = authenticatedUser.getEmail();
        String iban = authenticatedUser.getBankAccount()
                .getIban();
        String swift = authenticatedUser.getBankAccount()
                .getSwift();
        BigDecimal balance = authenticatedUser.getBankAccount()
                .getBalance();
        
        Transaction transaction = transactionService.getLastTransactionByBankAccountId(authenticatedUser.getBankAccount().getId());
        
        model.addAttribute("iban", iban);
        model.addAttribute("swift", swift);
        model.addAttribute("balance", balance);
        model.addAttribute("transaction", transaction);
        model.addAttribute("firstname", firstName);
        model.addAttribute("lastname", lastName);
        model.addAttribute("email", email);
        
        return "/html/authenticated/profile";
    }
    
    /**
     * Model to get data from profileModify form who modify Firstname, Lastname and email of the Principal User.
     *
     * @return a new UserModifyDto.
     */
    @ModelAttribute("userModifyDto")
    public UserModifyDto userModifyDto() {
        return new UserModifyDto();
    }
    
    /**
     * Model to get data from passwordModify form who modify the password of the Principal User.
     *
     * @return a new PasswordDto.
     */
    @ModelAttribute("passwordDto")
    public PasswordDto passwordDto() {
        return new PasswordDto();
    }
    
    /**
     * To get data from the profileModify form.
     * Call the save UserService method to save the Principal User updated.
     *
     * @param userModifyDto dto to modify the Principal User.
     * @return profile with param profileSuccess.
     */
    @PostMapping
    public String profileModify(
            @ModelAttribute("userModifyDto") UserModifyDto userModifyDto) {
        userService.save(userModifyDto);
        return "redirect:/profile?profileSuccess";
    }
    
    /**
     * To get fata form the passwordModify form.
     * Call passwordModifyCheck InputChecker to verify if all input are coherent.
     * Call save UserService method to save the Principal User updated.
     *
     * @param passwordDto Object to modify the principal's password.
     * @return the url to the profile page.
     * param passwordSuccess if all inputs are coherent and the new password has been saved successfully.
     * param passwordError if an input is not coherent. Old password check or new password check.
     */
    @PostMapping("/password")
    public String passwordModify(
            @ModelAttribute("passwordDto") PasswordDto passwordDto) {
        if(inputChecker.passwordModifyCheck(passwordDto)) {
            userService.save(passwordDto);
            return "redirect:/profile?passwordSuccess";
        }
        return "redirect:/profile?passwordError";
    }
    
    /**
     * To delete the Principal User account.
     * Call the deleteBy UserService method to delete the User from the User table.
     *
     * @return the url to login page with delete param.
     */
    @PostMapping("/delete")
    public String delete() {
        userService.deleteBy();
        return "redirect:/login?delete";
    }
}
