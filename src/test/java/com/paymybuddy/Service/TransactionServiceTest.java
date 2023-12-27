package com.paymybuddy.Service;

import com.paymybuddy.model.Transaction;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.service.TransactionService;
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
public class TransactionServiceTest {

@Autowired
    private TransactionService transactionService;

@MockBean
    private TransactionRepository transactionRepository;

private final Transaction TRANSACTION = new Transaction();
private final Optional<Transaction> optTransaction = Optional.of(TRANSACTION);
private final List<Transaction> transactionList = new ArrayList<>(List.of(new Transaction(), new Transaction()));

@Test
    public void getAllTest(){
    
    when(transactionRepository.findAll()).thenReturn(transactionList);
    
    Iterable<Transaction> result = transactionService.getAll();
    
    int resultSize = 0;
    
    for(Transaction t : transactionList){
        resultSize ++;
    }
    
    Assertions.assertEquals(transactionList.size(), resultSize);
}

@Test
    public void getBy(){
    
    when(transactionRepository.findById(1)).thenReturn(optTransaction);
    
    Optional<Transaction> result = transactionService.getBy(1);
    
    Assertions.assertEquals(optTransaction, result);
}
@Test
public void save(){
    
    when(transactionRepository.save(TRANSACTION)).thenReturn(TRANSACTION);
    
    Transaction result = transactionService.save(TRANSACTION);
    
    Assertions.assertEquals(TRANSACTION, result);
}

@Test
    public void deleteBy(){
    
    doNothing().when(transactionRepository).deleteById(1);
    
    transactionService.deleteBy(1);
    
    Assertions.assertDoesNotThrow(() -> transactionService.deleteBy(1));
    
}
}
