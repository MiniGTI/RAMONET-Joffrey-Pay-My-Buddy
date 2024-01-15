package com.paymybuddy.repository;

import com.paymybuddy.model.BankAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Crud repository interface for the BankAccount entity.
 * Perform requests with the bank_account table.
 */
@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, Integer> {
}
