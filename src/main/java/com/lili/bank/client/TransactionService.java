package com.lili.bank.client;

import com.lili.bank.common.exception.BankException;

/**
 * Define export services
 */
public interface TransactionService {
    /**
     * create one transaction
     * @param transactionDto
     * @return
     * @throws BankException
     */
    String createTransaction(TransactionDTO transactionDto) throws BankException;
    void deleteTransaction(String id);
    void modifyTransaction(ModifyTransactionDTO transactionDto);
    Page<TransactionDTO> listTransactions(TransactionQuery query);
}