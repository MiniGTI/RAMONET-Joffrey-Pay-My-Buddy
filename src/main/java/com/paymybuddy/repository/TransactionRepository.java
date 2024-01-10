package com.paymybuddy.repository;

import com.paymybuddy.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Crud repository interface for the Transaction model.
 * Perform requests with the transactions table.
 */
@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    
    /**
     * Query to get all transactions of a bankAccount with the bankAccount's id in a page format.
     *
     * @param id id of the bankAccount.
     * @param pageable the format parsed.
     * @return pages of transaction object.
     */
    @Query(value = "SELECT * FROM transactions WHERE bank_account_id = :id", nativeQuery = true)
    Page<Transaction> getPageTransactionsByBankAccountId(@Param("id") Integer id, Pageable pageable);
    
    /**
     * Query to get the last transaction of a bankAccount with the bankAccount's id.
     *
     * @param id id of the bankAccount.
     * @return a transaction object.
     */
    @Query(value = "SELECT * FROM transactions WHERE bank_account_id = :id ORDER BY created_at DESC LIMIT 1", nativeQuery = true)
    Transaction getTheLastTransactionByBankAccountId(@Param("id") Integer id);
}
