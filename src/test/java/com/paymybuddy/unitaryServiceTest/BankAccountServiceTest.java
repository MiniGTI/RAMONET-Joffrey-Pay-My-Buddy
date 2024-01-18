package com.paymybuddy.unitaryServiceTest;

import com.paymybuddy.dto.BankAccountDto;
import com.paymybuddy.dto.TransactionDto;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.BankAccountRepository;
import com.paymybuddy.service.BankAccountService;
import com.paymybuddy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class BankAccountServiceTest {
    
    @Autowired
    private BankAccountService bankAccountService;
    
    @MockBean
    private BankAccountRepository bankAccountRepository;
    @MockBean
    private UserService userService;
    private final List<Transaction> TRANSACTIONLIST = new ArrayList<>();
    private final BankAccount BANKACCOUNT =
            new BankAccount(1, new BigDecimal("1500.00"), "67acb3ed-6d46-4a15-ad02-d0d0be604fc1",
                    "6e3429a6-f593-45dc-8a93-1c9c3c4f32da", TRANSACTIONLIST);
    private final BankAccount BANKACCOUNT2 =
            new BankAccount(2, new BigDecimal("1000.00"), "301dcceb-49d9-47da-92cc-c386f88dfe4a",
                    "fc3e7eb7-4390-465d-8c54-2fcc65315d7a", new ArrayList<>());
    
    private final List<BankAccount> BANKACCOUNTLIST = new ArrayList<>(List.of(BANKACCOUNT, BANKACCOUNT2));
    
    private final User USER =
            new User(1, "usertest@email.com", "test", "firstnameTest", "lastnameTest", "USER", BANKACCOUNT);
    private final User USER2 =
            new User(2, "user2test@email.com", "test", "firstname2Test", "lastname2Test", "USER", BANKACCOUNT2);
    
    @Test
    void getAllTest() {
        
        when(bankAccountRepository.findAll()).thenReturn(BANKACCOUNTLIST);
        
        Iterable<BankAccount> result = bankAccountService.getAll();
        
        Assertions.assertEquals(BANKACCOUNTLIST, result);
    }
    
    @Test
    void getByTest() {
        
        when(bankAccountRepository.findById(1)).thenReturn(Optional.of(BANKACCOUNT));
        
        Optional<BankAccount> result = bankAccountService.getBy(1);
        
        Assertions.assertEquals(Optional.of(BANKACCOUNT), result);
    }
    
    @Test
    void saveTest() {
        
        when(bankAccountRepository.save(BANKACCOUNT)).thenReturn(BANKACCOUNT);
        
        BankAccount result = bankAccountService.save(BANKACCOUNT);
        
        Assertions.assertEquals(BANKACCOUNT, result);
    }
    
    @Test
    void deleteByTest() {
        
        doNothing().when(bankAccountRepository)
                .deleteById(1);
        
        bankAccountService.deleteBy(1);
        
        Assertions.assertDoesNotThrow(() -> bankAccountService.deleteBy(1));
    }
    
    @Test
    void shouldDepositWithBankAccountDtoTest() {
        BankAccountDto bankAccountDto = new BankAccountDto(new BigDecimal("50.00"));
        BankAccount updatedBankAccount =
                new BankAccount(1, new BigDecimal("1550.00"), "67acb3ed-6d46-4a15-ad02-d0d0be604fc1",
                        "6e3429a6-f593-45dc-8a93-1c9c3c4f32da", TRANSACTIONLIST);
        
        when(userService.getTheAuthenticatedUser()).thenReturn(USER);
        when(bankAccountRepository.save(updatedBankAccount)).thenReturn(updatedBankAccount);
        
        bankAccountService.deposit(bankAccountDto);
        
        verify(bankAccountRepository, Mockito.times(1)).save(updatedBankAccount);
    }
    
    @Test
    void shouldExternalWithBankAccountDtoTest() {
        BankAccountDto bankAccountDto = new BankAccountDto(new BigDecimal("50.00"));
        BankAccount updatedBankAccount =
                new BankAccount(1, new BigDecimal("1450.00"), "67acb3ed-6d46-4a15-ad02-d0d0be604fc1",
                        "6e3429a6-f593-45dc-8a93-1c9c3c4f32da", TRANSACTIONLIST);
        
        when(userService.getTheAuthenticatedUser()).thenReturn(USER);
        when(bankAccountRepository.save(updatedBankAccount)).thenReturn(updatedBankAccount);
        
        bankAccountService.external(bankAccountDto);
        
        verify(bankAccountRepository, Mockito.times(1)).save(updatedBankAccount);
    }
    
    @Test
    void shouldAbleToDepositWithTransactionDtoWhenBankAccountBalanceIsLessThanTheAmountTest() {
        TransactionDto transactionDto =
                new TransactionDto("user2test@email.com", new BigDecimal("5000.00"), "First transaction", BANKACCOUNT);
        
        when(userService.getTheAuthenticatedUser()).thenReturn(USER);
        
        Boolean result = bankAccountService.ableToDeposit(transactionDto);
        
        Assertions.assertFalse(result);
    }
    
    @Test
    void shouldAbleToDepositWithTransactionDtoWhenBankAccountBalanceIsMoreThanTheAmountTest() {
        TransactionDto transactionDto =
                new TransactionDto("user2test@email.com", new BigDecimal("50.00"), "First transaction", BANKACCOUNT);
        
        when(userService.getTheAuthenticatedUser()).thenReturn(USER);
        
        Boolean result = bankAccountService.ableToDeposit(transactionDto);
        
        Assertions.assertTrue(result);
    }
    
    @Test
    void shouldPerformTransactionWhenTheBuddyBankAccountIsNotFoundTest() {
        TransactionDto transactionDto =
                new TransactionDto("wrong@email.com", new BigDecimal("50.00"), "First transaction", BANKACCOUNT);
        
        when(userService.getByEmail(transactionDto.getConnection())).thenReturn(Optional.empty());
        
        RuntimeException runtimeException =
                Assertions.assertThrows(RuntimeException.class, () -> bankAccountService.transaction(transactionDto));
        
        Assertions.assertEquals("no user connexion found.", runtimeException.getMessage());
    }
    
    @Test
    void shouldPerformTransactionWhenAllIsRightTest() {
        TransactionDto transactionDto =
                new TransactionDto("user2test@email.com", new BigDecimal("50.00"), "First transaction", BANKACCOUNT);
        BankAccount updatedCreditAccount =
                new BankAccount(2, new BigDecimal("1050.00"), "301dcceb-49d9-47da-92cc-c386f88dfe4a",
                        "fc3e7eb7-4390-465d-8c54-2fcc65315d7a", new ArrayList<>());
        BankAccount updatedDebitAccount =
                new BankAccount(1, new BigDecimal("1447.50"), "67acb3ed-6d46-4a15-ad02-d0d0be604fc1",
                        "6e3429a6-f593-45dc-8a93-1c9c3c4f32da", TRANSACTIONLIST);
        BankAccount feeAccount = new BankAccount(1, new BigDecimal("0.00"), "32642962-9b0c-4655-95b7-d0d0be604fc1",
                "6e3429a6-f593-465d-92c5-92c5", null);
        
        when(userService.getByEmail(transactionDto.getConnection())).thenReturn(Optional.of(USER2));
        when(bankAccountRepository.save(USER2.getBankAccount())).thenReturn(updatedCreditAccount);
        when(bankAccountRepository.findById(1)).thenReturn(Optional.of(feeAccount));
        feeAccount.setBalance(new BigDecimal("2.5"));
        when(bankAccountRepository.save(feeAccount)).thenReturn(feeAccount);
        when(userService.getTheAuthenticatedUser()).thenReturn(USER);
        BANKACCOUNT.setBalance(new BigDecimal("1497.5"));
        when(bankAccountRepository.save(BANKACCOUNT)).thenReturn(BANKACCOUNT);
        when(userService.getTheAuthenticatedUser()).thenReturn(USER);
        when(bankAccountRepository.save(USER.getBankAccount())).thenReturn(updatedDebitAccount);
        
        bankAccountService.transaction(transactionDto);
        
        verify(bankAccountRepository, Mockito.times(4)).save(any(BankAccount.class));
        verify(bankAccountRepository, Mockito.times(1)).findById(1);
    }
}
