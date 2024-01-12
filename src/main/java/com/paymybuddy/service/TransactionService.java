package com.paymybuddy.service;

import com.paymybuddy.dto.TransactionDto;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for the Transaction object.
 * Perform all business processing between controllers and the TransactionRepository.
 */
@Service
public class TransactionService {
    
    /**
     * Call of SLF4J.
     */
    private final static Logger logger = LoggerFactory.getLogger(UserService.class);
    
    /**
     * Call the TransactionRepository to perform CRUDs request to the database.
     */
    private final TransactionRepository transactionsRepository;
    /**
     * Call the UserService to get data from User objects.
     */
    private final UserService userService;
    
    /**
     * The class constructor.
     *
     * @param transactionsRepository to perform CRUDs request to the database.
     * @param userService            to get data from User objects.
     */
    public TransactionService(TransactionRepository transactionsRepository, UserService userService) {
        this.transactionsRepository = transactionsRepository;
        this.userService = userService;
    }
    
    /**
     * Call the findAll method of the transaction repository.
     *
     * @return An iterable of all Transaction object present in the transactions table.
     */
    public Iterable<Transaction> getAll() {
        return transactionsRepository.findAll();
    }
    
    /**
     * Call the findById method of the transaction repository.
     *
     * @param id id of the Transaction object parsed.
     * @return The Transaction found.
     */
    public Optional<Transaction> getBy(Integer id) {
        return transactionsRepository.findById(id);
    }
    
    /**
     * Method to save a transaction.
     * Used in the transfer.html to create a new transaction object in the transactions table.
     *
     * @param transactionDto the dto object to get data from the transfer.html form.
     * @return call of the save transactionsRepository method.
     */
    public Transaction save(TransactionDto transactionDto) {
        
        BankAccount bankAccount = userService.getTheAuthenticatedUser()
                .getBankAccount();
        logger.debug("transactionDto bankAccount" + transactionDto.getBankAccount());
        logger.debug("bankAccount id: " + bankAccount.getId());
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
     * Method to get the last transaction of a bankAccount targeted by id.
     *
     * @param id id of the bankAccount.
     * @return a transaction object.
     */
    public Transaction getLastTransactionByBankAccountId(Integer id) {
        return transactionsRepository.getTheLastTransactionByBankAccountId(id);
    }
    
    /**
     * Method to get all transactions of the Principal User's bankAccount in a page format.
     *
     * @param numPage  id of the page.
     * @param sizePage the number of transaction per page.
     * @return a Page.
     */
    public Page<Transaction> getTransactionByBankAccountId(int numPage, int sizePage) {
        User authenticatedUser = userService.getTheAuthenticatedUser();
        
        Pageable pageable = PageRequest.of(numPage, sizePage);
        
        return transactionsRepository.getPageTransactionsByBankAccountId(authenticatedUser.getBankAccount()
                .getId(), pageable);
    }
}
