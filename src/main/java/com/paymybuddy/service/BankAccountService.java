package com.paymybuddy.service;

import com.paymybuddy.dto.TransactionDto;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.BankAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Optional;

/**
 * Service class for the BankAccount object.
 */
@Service
public class BankAccountService {
    
    /**
     * Logger class.
     */
    private final static Logger logger = LoggerFactory.getLogger(BankAccountService.class);
    
    /**
     * Call the BankAccountRepository.
     */
    private final BankAccountRepository bankAccountRepository;
    
    /**
     * Call the UserService.
     */
    private final UserService userService;
    
    /**
     * BankAccountService constructor.
     *
     * @param bankAccountRepository to access at the table of BankAccount of the Database.
     */
    public BankAccountService(BankAccountRepository bankAccountRepository, UserService userService) {
        this.bankAccountRepository = bankAccountRepository;
        this.userService = userService;
    }
    
    /**
     * Call the findAll method of the bankAccount repository.
     *
     * @return An iterable of all BankAccount object present in the Database's bankAccount table.
     */
    public Iterable<BankAccount> getAll() {
        return bankAccountRepository.findAll();
    }
    
    /**
     * Call the findById method of the bankAccount repository.
     *
     * @param id id of the User parsed.
     * @return The BankAccount object with the id parsed.
     */
    public Optional<BankAccount> getBy(int id) {
        return bankAccountRepository.findById(id);
    }
    
    /**
     * Call the save method of the bankAccount repository.
     *
     * @param bankAccount the new BankAccount object parsed to save.
     * @return call save method of the bankAccount repository.
     */
    public BankAccount save(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }
    
    /**
     * Call the deleteBy method of the bankAccount repository.
     *
     * @param id id of the BankAccount parsed.
     */
    public void deleteBy(int id) {
        bankAccountRepository.deleteById(id);
    }
    
    /**
     * Method to perform a transaction.
     * Debit money form the principal's bankAccount and credit the bankAccount of the connection parsed.
     *
     * @param principal user authenticated.
     * @param transactionDto the dto object to get data from the transfer form.
     */
    public void transaction(Principal principal, TransactionDto transactionDto) {
        BankAccount accountToDebit = getPrincipalBankAccount(principal);
        BigDecimal accountToDebitNewBalance = accountToDebit.getBalance()
                .subtract(transactionDto.getAmount());
        bankAccountRepository.transaction(accountToDebitNewBalance, accountToDebit.getId());
        
        Optional<User> optUserToCredit = userService.getByEmail(transactionDto.getConnection());
        BankAccount accountToCredit = null;
        BigDecimal accountToCreditNewBalance = null;
        if(optUserToCredit.isPresent()) {
            accountToCredit = optUserToCredit.get()
                    .getBankAccount();
            accountToCreditNewBalance = accountToCredit.getBalance()
                    .add(transactionDto.getAmount());
        } else {
            logger.error("No user connexion find.");
        }
        
        bankAccountRepository.transaction(accountToCreditNewBalance, accountToCredit.getId());
    }
    
    /**
     * Method to perform a deposit on the principal's bankAccount.
     *
     * @param balance The amount to parse.
     * @param userAccount the account id to deposit money.
     */
    public void deposit(BigDecimal balance, Integer userAccount) {
        bankAccountRepository.transaction(balance, userAccount);
    }
    
    /**
     * Method to verify if the principal's bankAccount can be pay the amount.
     *
     * @param principal user authenticated.
     * @param transactionDto the dto object to get data from the transfer form.
     * @return a boolean.
     */
    public Boolean ableToDeposit(Principal principal, TransactionDto transactionDto) {
        
        BigDecimal balance = getPrincipalBankAccount(principal).getBalance();
        BigDecimal amount = transactionDto.getAmount();
        
        BigDecimal subtract = balance.subtract(amount);
        return (subtract.compareTo(BigDecimal.ZERO) > 0);
    }
    
    /**
     * Method to get the principal's bankAccount.
     *
     * @param principal user authenticated.
     * @return a bankAccount object.
     */
    private BankAccount getPrincipalBankAccount(Principal principal){
        
        Optional<User> optUser = userService.getBy(userService.getPrincipalId(principal));
        BankAccount bankAccount = new BankAccount();
        if(optUser.isPresent()) {
            bankAccount = optUser.get()
                    .getBankAccount();
        }
        
        return bankAccount;
    }
}
