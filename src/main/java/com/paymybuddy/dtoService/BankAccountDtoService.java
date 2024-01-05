package com.paymybuddy.dtoService;

import com.paymybuddy.dto.BankAccountDto;
import com.paymybuddy.model.User;
import com.paymybuddy.service.BankAccountService;
import com.paymybuddy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Optional;

/**
 * The service class for the BankAccountDto object.
 */
@Service
public class BankAccountDtoService {
    
    /**
     * Logger class.
     */
    private static final Logger logger = LoggerFactory.getLogger(BankAccountDtoService.class);
    
    /**
     * To call the UserService.
     */
    private final UserService userService;
    /**
     * To call the BankAccountService.
     */
    private final BankAccountService bankAccountService;
    
    /**
     * Constructor of BankAccountDtoService.
     *
     * @param userService to access at the user service class.
     * @param bankAccountService to access at the bankAccount service class.
     */
    public BankAccountDtoService(UserService userService, BankAccountService bankAccountService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
    }
    
    /**
     * Method to update the principal balance.
     *
     * @param principal user authenticated.
     * @param bankAccountDto dto to parse the amount.
     */
    
    public void deposit(Principal principal, BankAccountDto bankAccountDto) {
        Optional<User> optUser = userService.getByEmail(principal.getName());
        User user = new User();
        
        if(optUser.isPresent()) {
            user = optUser.get();
        } else {
            logger.error("No principal user find");
        }
        
        Integer userAccount = user.getBankAccount()
                .getId();
        BigDecimal balance = user.getBankAccount()
                .getBalance()
                .add(bankAccountDto.getAmount());
        
        bankAccountService.deposit(balance, userAccount);
    }
}
