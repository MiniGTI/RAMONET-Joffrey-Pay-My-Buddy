package com.paymybuddy.repository;

import com.paymybuddy.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Crud repository interface for the Transaction model.
 */
@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
}
