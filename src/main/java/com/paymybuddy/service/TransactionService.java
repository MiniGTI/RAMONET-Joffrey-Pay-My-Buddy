package com.paymybuddy.service;

import com.paymybuddy.dto.TransactionDto;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

/**
 * Service class for the Transaction object.
 */
@Service
public class TransactionService {
    /**
     * Call the TransactionRepository.
     */
    private final TransactionRepository transactionsRepository;
    /**
     * Call the UserService.
     */
    private final UserService userService;
    
    /**
     * TransactionService constructor.
     *
     * @param transactionsRepository to access at the table of transaction of the Database.
     */
    public TransactionService(TransactionRepository transactionsRepository, UserService userService) {
        this.transactionsRepository = transactionsRepository;
        this.userService = userService;
    }
    
    /**
     * Call the findAll method of the transaction repository.
     *
     * @return An iterable of all Transaction object present in the Database's transaction table.
     */
    public Iterable<Transaction> getAll() {
        return transactionsRepository.findAll();
    }
    
    /**
     * Call the findById method of the transaction repository.
     *
     * @param id id of the Transaction parsed.
     * @return The Transaction object with the id parsed.
     */
    public Optional<Transaction> getBy(Integer id) {
        return transactionsRepository.findById(id);
    }
    
    
    /**
     * Call the save method of the user repository.
     *
     * @param transaction the Transaction object parsed to save in the Database/s transaction table.
     * @return call save method of the transaction repository with the Transaction object parsed.
     */
    public Transaction save(Transaction transaction) {
        return transactionsRepository.save(transaction);
    }
    
    /**
     * Method to save a transaction.
     *
     * @param principal user authenticated.
     * @param transactionDto the dto object to get data from the transfer form.
     * @return call of the transactionsRepository.
     */
    public Transaction save(Principal principal, TransactionDto transactionDto) {
        
        Optional<User> optUser = userService.getBy(userService.getPrincipalId(principal));
        BankAccount bankAccount = new BankAccount();
        if(optUser.isPresent()) {
            bankAccount = optUser.get()
                    .getBankAccount();
        }
        Transaction transaction = new Transaction(transactionDto.getDescription(), transactionDto.getConnection(),
                transactionDto.getAmount(), bankAccount);
        return transactionsRepository.save(transaction);
    }
    
    /**
     * Method to delete a transaction by id.
     *
     * @param id id of the transaction.
     */
    public void deleteBy(Integer id) {
        transactionsRepository.deleteById(id);
    }
    
    /**
     * Method to get the las transaction of a bankAccount.
     *
     * @param id id of the bankAccount.
     * @return the transaction.
     */
    public Transaction getLastTransactionByBankAccountId(Integer id) {
        return transactionsRepository.getTheLastTransactionByBankAccountId(id);
    }
    
    /**
     * Method to get all transactions of a bankAccount in a page format.
     *
     * @param numPage id of the page.
     * @param sizePage transaction number per page.
     * @return a Page.
     */
    public Page<Transaction> getTheLastTransactionByBankAccountId(int numPage, int sizePage) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        
        String name = authentication.getName();
        
        Optional<User> optionalUser = userService.getByEmail(name);
        
        if(optionalUser.isEmpty()) {
            throw new RuntimeException("no user find");
        }
        
        Integer bankAccountId = optionalUser.get()
                .getBankAccount()
                .getId();
        
        Pageable pageable = PageRequest.of(numPage, sizePage);
        
        return transactionsRepository.getPageTransactionsByBankAccountId(bankAccountId, pageable);
    }
}
