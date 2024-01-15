package com.paymybuddy.controller;


import com.paymybuddy.dto.BankAccountDto;
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


/**
 * Controller class for the home.html.
 * This is the home page.
 * Page to view the last Principal User's transaction.
 * Page how the Principal user can deposit money from an external account.
 * Page to see the Principal's User IBAN, SWIFT and BankAccount balance.
 * Required an authentication, if no remember-me token present, redirect to the login page.
 */
@Controller
@RequestMapping("/")
public class HomeController {
    /**
     * Call the BankAccountService to get data from BankAccount objects.
     */
    private final BankAccountService bankAccountService;
    /**
     * Call the UserService to get data from User objects.
     */
    private final UserService userService;
    /**
     * Call the TransactionService to get data from Transaction objects.
     */
    private final TransactionService transactionService;
    
    /**
     * The class constructor.
     *
     * @param bankAccountService to get data from BankAccount objects.
     * @param userService        to get data from User objects.
     * @param transactionService to get data from Transaction objects.
     */
    public HomeController(BankAccountService bankAccountService, UserService userService,
                          TransactionService transactionService) {
        this.bankAccountService = bankAccountService;
        this.userService = userService;
        this.transactionService = transactionService;
    }
    
    /**
     * BankAccountDto model for the form.
     * To add money to the Principal User's bankAccount.
     *
     * @return new BankAccountDto.
     */
    @ModelAttribute("bankAccountDto")
    public BankAccountDto bankAccountDto() {
        return new BankAccountDto();
    }
    
    /**
     * To get data for all model of the Home page.
     * - The last transaction of the Principal User.
     * - IBAN , SWIFT, BALANCE of the Principal User's bankAccount.
     * Call getTheAuthenticatedUser UserService to get the Principal User.
     * Call the getLastTransactionByBankAccountId TransactionService to get the last Principal User transaction.
     *
     * @param model to parse data to the view.
     * @return home.html.
     */
    @GetMapping
    public String accountField(Model model) {
        User authenticatedUser = userService.getTheAuthenticatedUser();
        
        String iban = authenticatedUser.getBankAccount()
                .getIban();
        String swift = authenticatedUser.getBankAccount()
                .getSwift();
        BigDecimal balance = authenticatedUser.getBankAccount()
                .getBalance();
        
        Transaction transaction = transactionService.getLastTransactionByBankAccountId(
                authenticatedUser.getBankAccount()
                        .getId());
        
        model.addAttribute("iban", iban);
        model.addAttribute("swift", swift);
        model.addAttribute("balance", balance);
        model.addAttribute("transaction", transaction);
        return "/html/authenticated/home";
    }
    
    /**
     * To get the deposit amount.
     * Call the deposit bankAccountService method to update the Principal User's bankAccount balance with the money add.
     *
     * @param bankAccountDto The dto to parse the amount value.
     * @return home.html.
     */
    @PostMapping
    public String deposite(
            @ModelAttribute("bankAccountDto") BankAccountDto bankAccountDto) {
        
        bankAccountService.deposit(bankAccountDto);
        return "redirect:/";
    }
    
    /**
     * To get the out amount.
     * Call the external bankAccountService method to update the Principal User's bankAccount balance with the money out.
     *
     * @param bankAccountDto The dto to parse the amount value.
     * @return home.html.
     */
    @PostMapping("/out")
    public String external(
            @ModelAttribute("bankAccountDto") BankAccountDto bankAccountDto) {
        
        bankAccountService.external(bankAccountDto);
        return "redirect:/";
    }
}
