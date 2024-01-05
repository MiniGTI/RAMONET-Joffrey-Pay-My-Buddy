package com.paymybuddy.repository;

import com.paymybuddy.model.BankAccount;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Crud repository interface for the BankAccount model.
 */
@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, Integer> {
    /**
     * Query to update the balance of an account with the bankAccount id.
     * @param balance the amount to update the balance.
     * @param id the id of the account.
     */
    @Modifying
    @Transactional
    @Query(
            value = "UPDATE bank_account SET balance = :balance WHERE id =:id",
            nativeQuery = true)
    void transaction(
            @Param("balance") BigDecimal balance,
            @Param("id") Integer id);
}
