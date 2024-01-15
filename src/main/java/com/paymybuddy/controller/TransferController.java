package com.paymybuddy.controller;

import com.paymybuddy.dto.TransactionDto;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.service.BankAccountService;
import com.paymybuddy.service.TransactionService;
import com.paymybuddy.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller class for the transfer.html.
 * Page to transfer money from the Principal User to a User from his buddyList.
 * Page to see history of the Principal User's BankAccount transactions.
 * Page to access to the addBuddy form.
 * Required an authentication, if no remember-me token present, redirect to the login page.
 */
@Controller
@RequestMapping("/transfer")
public class TransferController {
    
    /**
     * Call the TransactionService to get data from Transaction objects.
     */
    private final TransactionService transactionService;
    
    /**
     * Call the BankAccountService to get data from BankAccount objects.
     */
    private final BankAccountService bankAccountService;
    
    /**
     * Call the UserService to get data from User objects.
     */
    private final UserService userService;
    
    /**
     * The class constructor.
     *
     * @param transactionService to access at the transaction service class.
     * @param bankAccountService to access at the bankAccount service class.
     * @param userService        to get data from User objects.
     */
    public TransferController(TransactionService transactionService, BankAccountService bankAccountService,
                              UserService userService) {
        this.transactionService = transactionService;
        this.bankAccountService = bankAccountService;
        this.userService = userService;
    }
    
    /**
     * TransactionDto model for the transfer form.
     * To parse data to perform a transaction.
     *
     * @return new TransactionDto.
     */
    @ModelAttribute("transactionDto")
    public TransactionDto transactionDto() {
        return new TransactionDto();
    }
    
    /**
     * Url to access at the transfer.html.
     * To parse DATAs to the transaction history.
     * Call getAllBuddy UserService method to get the Principal User's buddyList.
     * Call getTransactionsByBankAccountId TransactionService method to get all Principal User's BankAccount transactions by page of 3 buddy.
     *
     * @param model to parse data to the view.
     * @param page  number of the page.
     * @return transfer.html.
     */
    @GetMapping
    public String transfer(Model model,
                           @RequestParam(defaultValue = "0") Integer page) {
        int pageSize = 3;
        
        List<User> buddyList = userService.getAllBuddy();
        
        Page<Transaction> transactionsPage = transactionService.getTransactionByBankAccountId(page, pageSize);
        
        List<Transaction> transactions = transactionsPage.getContent();
        
        model.addAttribute("buddyList", buddyList);
        model.addAttribute("transactions", transactions);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", transactionsPage.getTotalPages());
        model.addAttribute("totalItem", transactionsPage.getTotalElements());
        return "/html/authenticated/transfer";
    }
    
    /**
     * To parse data from the transfer form to perform a money transfer.
     * Call ableToDeposit BankAccountService method to check if the Principal User's BankAccount is solvent.
     * Call save TransactionService method to save a new transaction linked to the Principal User's bankAccount.
     * Call transaction BankAccountService method to apply balance modify to the Principal User's BankAccount and the buddy's BankAccount.
     *
     * @param transactionDto the transactionDto create from the form to the services.
     * @return transfer.html
     * param errorBalance if the Principal User's BankAccount isn't solvent.
     * param error0Balance if the amount input is equals to 0.
     * param success if the transfer is successful.
     */
    @PostMapping
    public String connexion(
            @ModelAttribute("transactionDto") TransactionDto transactionDto) {
        
        if(!bankAccountService.ableToDeposit(transactionDto)) {
            return "redirect:/transfer?errorBalance";
        }
        
        if(transactionDto.getAmount()
                .compareTo(BigDecimal.ZERO) <= 0) {
            return "redirect:/transfer?error0Balance";
        }
        
        transactionService.save(transactionDto);
        bankAccountService.transaction(transactionDto);
        
        return "redirect:/transfer?success";
    }
}
