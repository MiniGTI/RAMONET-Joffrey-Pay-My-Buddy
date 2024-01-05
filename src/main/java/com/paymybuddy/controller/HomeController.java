package com.paymybuddy.controller;


import com.paymybuddy.dto.BankAccountDto;
import com.paymybuddy.dtoService.BankAccountDtoService;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.Transaction;
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
 * Controller class for the home.html.
 */
@Controller
@RequestMapping("/")
public class HomeController {
    /**
     * Call the BankAccountDtoService.
     */
    private final BankAccountDtoService bankAccountDtoService;
    /**
     * Call the BankAccountService.
     */
    private final BankAccountService bankAccountService;
    /**
     * Call the UserService.
     */
    private final UserService userService;
    /**
     * Call the TransactionService.
     */
    private final TransactionService transactionService;
    
    /**
     * HomeController constructor.
     *
     * @param bankAccountDtoService to access at the bankAccountDto service class.
     * @param bankAccountService to access at the bankAccount service class.
     * @param userService to access at the user service class.
     * @param transactionService to access at the transaction service class.
     */
    public HomeController(BankAccountDtoService bankAccountDtoService, BankAccountService bankAccountService,
                          UserService userService, TransactionService transactionService) {
        this.bankAccountDtoService = bankAccountDtoService;
        this.bankAccountService = bankAccountService;
        this.userService = userService;
        this.transactionService = transactionService;
    }
    
    /**
     * BankAccountDto model for the form.
     *
     * @return new BankAccountDto.
     */
    @ModelAttribute("bankAccountDto")
    public BankAccountDto bankAccountDto() {
        return new BankAccountDto();
    }
    
    /**
     * To get data for all model of the Home page.
     *      - row last Transaction.
     *      - IBAN , SWIFT, BALANCE
     *
     * @param principal user authenticated.
     * @param model to parse data to the view.
     * @return home.html.
     */
    @GetMapping
    public String accountField(Principal principal, Model model) {
        String iban = null;
        String swift = null;
        BigDecimal balance = null;
        Integer accountId = null;
        
        Integer principalId = userService.getPrincipalId(principal);
        Optional<BankAccount> optAccount = bankAccountService.getBy(principalId);
        
        if(optAccount.isPresent()) {
            iban = optAccount.get()
                    .getIban();
            swift = optAccount.get()
                    .getSwift();
            balance = optAccount.get()
                    .getBalance();
            accountId = optAccount.get().getId();
        }
        Transaction transaction =
                transactionService.getLastTransactionByBankAccountId(accountId);
        
        model.addAttribute("iban", iban);
        model.addAttribute("swift", swift);
        model.addAttribute("balance", balance);
        model.addAttribute("transaction", transaction);
        return "/html/authenticated/home";
    }
    
    /**
     * To get the deposit amount.
     *
     * @param bankAccountDto The dto to parse the amount value.
     * @param principal user authenticated.
     * @return home.html.
     */
    @PostMapping
    public String deposite(
            @ModelAttribute("bankAccountDto") BankAccountDto bankAccountDto, Principal principal) {
        
        bankAccountDtoService.deposit(principal, bankAccountDto);
        return "redirect:/";
    }
}
