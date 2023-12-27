package com.paymybuddy.Service;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.repository.BankAccountRepository;
import com.paymybuddy.service.BankAccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BankAccountServiceTest {

@Autowired
    private BankAccountService bankAccountService;

@MockBean
    private BankAccountRepository bankAccountRepository;


private final BankAccount bankAccount = new BankAccount();

private final List<BankAccount> bankAccountList = new ArrayList<>(List.of(new BankAccount(), new BankAccount()));

private final Optional<BankAccount> optBankAccount = Optional.of(bankAccount);


@Test
    public void getAllTest(){
    
    when(bankAccountRepository.findAll()).thenReturn(bankAccountList);
    
    Iterable<BankAccount> result = bankAccountService.getAll();
    
    int resultSize =0;
    for(BankAccount b : result){
        resultSize++;
    }
    
    Assertions.assertEquals(bankAccountList.size(), resultSize);
}

@Test
    public void getByTest(){
    
    when(bankAccountRepository.findById(1)).thenReturn(optBankAccount);
    
    Optional<BankAccount> result = bankAccountService.getBy(1);
    
    Assertions.assertEquals(optBankAccount, result);
}

@Test
    public void saveTest(){
    
    when(bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);
    
    BankAccount result = bankAccountService.save(bankAccount);
    
    Assertions.assertEquals(bankAccount, result);
}

@Test
    public void deleteBy(){
    
    doNothing().when(bankAccountRepository).deleteById(1);
    
    bankAccountService.deleteBy(1);
    
    Assertions.assertDoesNotThrow(() -> bankAccountService.deleteBy(1));
}

}
