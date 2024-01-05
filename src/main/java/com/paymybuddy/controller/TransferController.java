package com.paymybuddy.controller;

import com.paymybuddy.dto.TransactionDto;
import com.paymybuddy.dtoService.BuddyDtoService;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.service.BankAccountService;
import com.paymybuddy.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Controller class for the transfer.html.
 */
@Controller
@RequestMapping("/transfer")
public class TransferController {
    /**
     * Call the BuddyDtoService.
     */
    private final BuddyDtoService buddyDtoService;
    /**
     * Call the TransactionService.
     */
    private final TransactionService transactionService;

    /**
     * Call the BankAccountService.
     */
    private final BankAccountService bankAccountService;
    
    /**
     * TransferController constructor.
     *
     * @param buddyDtoService    to access at the buddyDto service class.
     * @param transactionService to access at the transaction service class.
     * @param bankAccountService to access at the bankAccount service class.
     */
    public TransferController(BuddyDtoService buddyDtoService, TransactionService transactionService, BankAccountService bankAccountService) {
        this.buddyDtoService = buddyDtoService;
        this.transactionService = transactionService;
        this.bankAccountService = bankAccountService;
    }
    
    /**
     * transactionDto model for the form.
     *
     * @return new TransactionDto.
     */
    @ModelAttribute("transactionDto")
    public TransactionDto transactionDto() {
        return new TransactionDto();
    }
    
    /**
     * To get DATAs for the transaction history.
     *
     * @param principal user authenticated.
     * @param model to parse data to the view.
     * @param page number of the page.
     * @return transfer.html.
     */
    @GetMapping
    public String transfer(Principal principal, Model model,
                           @RequestParam(defaultValue = "0") Integer page) {
        Integer pageSize = 3;
        
        List<User> buddyList = buddyDtoService.getBuddys(principal);
        
        Page<Transaction> transactionsPage =
                transactionService.getTheLastTransactionByBankAccountId(page, pageSize);
        
        List<Transaction> transactions = transactionsPage.getContent();
        
        model.addAttribute("buddyList", buddyList);
        model.addAttribute("transactions", transactions);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", transactionsPage.getTotalPages());
        model.addAttribute("totalItem", transactionsPage.getTotalElements());
        return "/html/authenticated/transfer";
    }
    
    /**
     * To get the data from the form to perform a money transfer.
     *
     * @param principal user authenticated.
     * @param transactionDto the dto object to parse data from the form to the services.
     * @return transfer.html with success param.
     */
    @PostMapping
    public String connexion(Principal principal,
                            @ModelAttribute("transactionDto") TransactionDto transactionDto) {
        
        if(!bankAccountService.ableToDeposit(principal, transactionDto)) {
            return "redirect:/transfer?errorBalance";
        } else {
            transactionService.save(principal, transactionDto);
            bankAccountService.transaction(principal, transactionDto);
        }
        return "redirect:/transfer?success";
    }
}
