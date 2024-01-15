package com.paymybuddy.service;

import com.paymybuddy.dto.BankAccountDto;
import com.paymybuddy.dto.TransactionDto;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.BankAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Service class for the BankAccount object.
 * Perform all business processing between controllers and the BankAccountRepository.
 */
@Service
public class BankAccountService {
    
    /**
     * Call of SLF4J.
     */
    private final static Logger logger = LoggerFactory.getLogger(UserService.class);
    
    
    /**
     * Call the BankAccountRepository to perform CRUDs request to the database.
     */
    private final BankAccountRepository bankAccountRepository;
    
    /**
     * Call the UserService to get data from User objects.
     */
    private final UserService userService;
    
    /**
     * The class constructor.
     *
     * @param bankAccountRepository to perform CRUDs request to the database.
     * @param userService           to get data from User objects.
     */
    public BankAccountService(BankAccountRepository bankAccountRepository, UserService userService) {
        this.bankAccountRepository = bankAccountRepository;
        this.userService = userService;
    }
    
    /**
     * Call the findAll method of the bankAccount repository.
     *
     * @return An iterable of all BankAccount object present in the bank_account table.
     */
    public Iterable<BankAccount> getAll() {
        return bankAccountRepository.findAll();
    }
    
    /**
     * Call the findById method of the bankAccount repository.
     *
     * @param id id of the BankAccount object parsed.
     * @return The BankAccount found.
     */
    public Optional<BankAccount> getBy(int id) {
        return bankAccountRepository.findById(id);
    }
    
    /**
     * Call the save method of the bankAccount repository.
     * Used to update bankAccounts in the bank_account table with the new balance after operations.
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
     * Debit money form the principal's bankAccount, credit the bankAccount of the connection and get the fee into the feeAccount .
     * First update the connection's bankAccount balance with the new BigDecimal after addition with the amount.
     * Call feeCollect method to collect the fee into the feeAccount.
     * And update the Principal user's bankAccount balance with the new BigDecimal after the subtraction with the transactionDto amount attribute.
     *
     * @param transactionDto the dto object to get data from the transfer.html form.
     */
    public void transaction(TransactionDto transactionDto) {
        BigDecimal amount = transactionDto.getAmount();
        BigDecimal fee = feeCalculator(amount);
        logger.debug("transactionDto amount: " + transactionDto.getAmount() + " fee: " + fee);
        
        Optional<User> optUserToCredit = userService.getByEmail(transactionDto.getConnection());
        
        if(optUserToCredit.isPresent()) {
            BankAccount bankAccountToCredit = optUserToCredit.get()
                    .getBankAccount();
            logger.debug("BankAccount id: " + bankAccountToCredit.getId() + " balance before: " +
                    bankAccountToCredit.getBalance());
            bankAccountToCredit.setBalance(bankAccountToCredit.getBalance()
                    .add(transactionDto.getAmount()));
            logger.debug("Balance after: " + bankAccountToCredit.getBalance());
            bankAccountRepository.save(bankAccountToCredit);
        } else {
            throw new RuntimeException("no user connexion found.");
        }
        
        feeCollect(fee);
        
        BankAccount authiticatedBankAccount = userService.getTheAuthenticatedUser()
                .getBankAccount();
        logger.debug("Principal BankAccount id: " + authiticatedBankAccount.getId() + " balance before: " +
                authiticatedBankAccount.getBalance());
        authiticatedBankAccount.setBalance(authiticatedBankAccount.getBalance()
                .subtract(amount));
        logger.debug("Balance after: " + authiticatedBankAccount.getBalance());
        bankAccountRepository.save(authiticatedBankAccount);
    }
    
    /**
     * Method to update the principal balance after external deposit.
     * For the moment it's juste a simple decimal input.
     * Update the Principal user's bankAccount balance with the new BigDecimal after the addition with the input valor.
     *
     * @param bankAccountDto dto to parse the amount from the deposit form in the home.html.
     */
    public void deposit(BankAccountDto bankAccountDto) {
        logger.debug("bankAccountDto amount: " + bankAccountDto.getAmount());
        BankAccount authenticatedBankAccount = userService.getTheAuthenticatedUser()
                .getBankAccount();
        logger.debug("Principal BankAccount id: " + authenticatedBankAccount.getId() + " balance before: " +
                authenticatedBankAccount.getBalance());
        authenticatedBankAccount.setBalance(authenticatedBankAccount.getBalance()
                .add(bankAccountDto.getAmount()));
        logger.debug("Balance after: " + authenticatedBankAccount.getBalance());
        bankAccountRepository.save(authenticatedBankAccount);
    }
    
    /**
     * Method to update the principal balance after external retirement.
     * For the moment it's juste a simple decimal output.
     * Update the Principal user's bankAccount balance with the new BigDecimal after subtract the input valor.
     *
     * @param bankAccountDto dto to parse the amount from the external form in the home.html.
     */
    public void external(BankAccountDto bankAccountDto) {
        logger.debug("bankAccountDto amount: " + bankAccountDto.getAmount());
        BankAccount authenticatedBankAccount = userService.getTheAuthenticatedUser()
                .getBankAccount();
        logger.debug("Principal BankAccount id: " + authenticatedBankAccount.getId() + " balance before: " +
                authenticatedBankAccount.getBalance());
        authenticatedBankAccount.setBalance(authenticatedBankAccount.getBalance()
                .subtract(bankAccountDto.getAmount()));
        logger.debug("Balance after: " + authenticatedBankAccount.getBalance());
        bankAccountRepository.save(authenticatedBankAccount);
    }
    
    /**
     * Method to verify if the principal's bankAccount can be pay the amount.
     * It's use in the transaction operation, in the transferController to make an error before the transaction if false.
     * A simple subtraction between the bankAccount balance and the transaction amount after add the fee, and a check if is greater than 0.
     *
     * @param transactionDto the dto object to get the amount from the transfer form.
     * @return true if greater than 0 and can pay, false if less to 0 and can't pay.
     */
    public Boolean ableToDeposit(TransactionDto transactionDto) {
        BigDecimal amount = transactionDto.getAmount();
        BigDecimal fee = feeCalculator(amount);
        BigDecimal totalRequiredAmount = amount.add(fee);
        
        BigDecimal balance = userService.getTheAuthenticatedUser()
                .getBankAccount()
                .getBalance();
        
        BigDecimal subtract = balance.subtract(totalRequiredAmount);
        logger.debug("transactionDto connexion: " + transactionDto.getConnection() + " amount: " +
                transactionDto.getAmount() + " bankAccount: " + transactionDto.getBankAccount() + " fee calculate: " +
                fee + " totalRequiredAmount: " + totalRequiredAmount + " Principal bankAccount balance: " + balance +
                " account balance with transaction amount subtract: " + subtract);
        
        return (subtract.compareTo(BigDecimal.ZERO) > 0);
    }
    
    /**
     * Method to subtract five per cent of the transaction.
     * Get the amount of the transaction, and multiply him per 0.05.
     *
     * @param amount the BigDecimal of the transaction.
     * @return the BigDecimal of the fee valor.
     */
    private BigDecimal feeCalculator(BigDecimal amount) {
        BigDecimal fee = new BigDecimal("0.05");
        logger.debug("Amount parsed: " + amount);
        return amount.multiply(fee);
    }
    
    /**
     * Method to collect the fee.
     * First add the fee to the feeAccount.
     * And subtract the fee amount to the principal bankAccount.
     * The feeAccount has id = 1.
     *
     * @param amount the BigDecimal of the fee to collect.
     */
    private void feeCollect(BigDecimal amount) {
        Optional<BankAccount> optFeeAccount = bankAccountRepository.findById(1);
        if(optFeeAccount.isPresent()) {
            BankAccount feeAccount = optFeeAccount.get();
            logger.debug("feeAccount balance before: " + feeAccount.getBalance());
            feeAccount.setBalance(feeAccount.getBalance()
                    .add(amount));
            logger.debug("feeAccount after: " + feeAccount.getBalance());
            bankAccountRepository.save(feeAccount);
        } else {
            throw new RuntimeException("Fee account not found.");
        }
        
        BankAccount principalBankAccount = userService.getTheAuthenticatedUser()
                .getBankAccount();
        logger.debug("Principal bankAccount balance before: " + principalBankAccount.getBalance());
        principalBankAccount.setBalance(principalBankAccount.getBalance()
                .subtract(amount));
        logger.debug("Principal bankAccount balance after: " + principalBankAccount.getBalance());
        bankAccountRepository.save(principalBankAccount);
    }
}