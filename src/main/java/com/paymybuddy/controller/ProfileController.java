package com.paymybuddy.controller;

import com.paymybuddy.dto.PasswordDto;
import com.paymybuddy.dto.UserModifyDto;
import com.paymybuddy.dtoService.PasswordDtoService;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.service.BankAccountService;
import com.paymybuddy.service.TransactionService;
import com.paymybuddy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Optional;

/**
 * Controller class for the profile.html.
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {
    /**
     * Call the UserService.
     */
    private final UserService userService;
    /**
     * Call the BankAccountService.
     */
    private final BankAccountService bankAccountService;
    /**
     * Call the TransactionService.
     */
    private final TransactionService transactionService;
    /**
     * Call the PasswordDtoService.
     */
    private final PasswordDtoService passwordDtoService;
    
    /**
     * ProfileController constructor.
     *
     * @param bankAccountService to access at the bankAccount service class.
     * @param userService to access at the user service class.
     * @param transactionService to access at the transaction service class.
     * @param passwordDtoService to acces at the passwordDtoService
     */
    public ProfileController(UserService userService, BankAccountService bankAccountService,
                             TransactionService transactionService, PasswordDtoService passwordDtoService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
        this.passwordDtoService = passwordDtoService;
    }
    
    /**
     * To manage all data fields.
     *
     * @param principal user authenticated.
     * @param model to parse data to the view.
     * @return profile.html
     */
    @GetMapping
    public String profile(Principal principal, Model model) {
        String iban = null;
        String swift = null;
        BigDecimal balance = null;
        Integer accountId = null;
        String firstName = null;
        String lastName = null;
        String email = null;
        
        Optional<User> optUser = userService.getByEmail(principal.getName());
        
        if(optUser.isPresent()) {
            firstName = optUser.get()
                    .getFirstname();
            lastName = optUser.get()
                    .getLastname();
            email = optUser.get()
                    .getEmail();
        }
        
        Integer principalId = userService.getPrincipalId(principal);
        Optional<BankAccount> optAccount = bankAccountService.getBy(principalId);
        
        if(optAccount.isPresent()) {
            iban = optAccount.get()
                    .getIban();
            swift = optAccount.get()
                    .getSwift();
            balance = optAccount.get()
                    .getBalance();
            accountId = optAccount.get()
                    .getId();
        }
        Transaction transaction = transactionService.getLastTransactionByBankAccountId(accountId);
        
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
     * Model to get data from the form who modify Firstname, Lastname, email of the principal.
     *
     * @return a new UserModifyDto.
     */
    @ModelAttribute("userModifyDto")
    public UserModifyDto userModifyDto() {
        return new UserModifyDto();
    }
    
    /**
     * Model to get data from the form who modify the password of the principal.
     *
     * @return a new PasswordDto.
     */
    @ModelAttribute("passwordDto")
    public PasswordDto passwordDto() {
        return new PasswordDto();
    }
    
    /**
     * To get data from the modify form.
     *
     * @param userModifyDto Object to modify the principal account.
     * @return profile with param profileSuccess.
     */
    @PostMapping
    public String profileModify(
            @ModelAttribute("userModifyDto") UserModifyDto userModifyDto) {
        userService.save(userModifyDto);
        System.out.println(userModifyDto);
        return "redirect:/profile?profileSuccess";
    }
    
    /**
     * To get fata form the password form.
     *
     * @param passwordDto Object to modify the principal's password.
     * @return profile with passwordError or passwordSuccess param.
     */
    @PostMapping("/password")
    public String passwordModify(
            @ModelAttribute("passwordDto") PasswordDto passwordDto) {
        if(!passwordDtoService.passwordModifyCheck(passwordDto)) {
            userService.save(passwordDto);
            return "redirect:/profile?passwordSuccess";
        }
        return "redirect:/profile?passwordError";
    }
    
    /**
     * To delete the principal account.
     *
     * @param principal user authenticated.
     * @return login with delete param.
     */
    @PostMapping("/delete")
    public String delete(Principal principal) {
        Integer principalId = userService.getPrincipalId(principal);
        userService.deleteBy(principalId);
        return "redirect:/login?delete";
    }
}
