package com.lili.bank.application;

import com.lili.bank.client.*;
import com.lili.bank.common.utils.ObjectUtils;
import com.lili.bank.domain.transaction.TransactionEntity;
import com.lili.bank.domain.transaction.TransactionManager;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TransactionAppService implements TransactionService {
    @Autowired
    TransactionManager transactionManager;


    @Override
    public String createTransaction(TransactionDTO transactionDto) {
        return transactionManager.createTransaction(ObjectUtils.convertObject(transactionDto, TransactionEntity.class));
    }

    @Override
    public void deleteTransaction(String id) {
        transactionManager.deleteTransaction(id);
    }

    @Override
    public void modifyTransaction(ModifyTransactionDTO transactionDto) {
        transactionManager.modifyTransaction(ObjectUtils.convertObject(transactionDto, TransactionEntity.class));
    }

    @Override
    public Page<TransactionDTO> listTransactions(TransactionQuery query) {
        Page<TransactionEntity> entityPage= transactionManager.listTransactions(query);
        return new Page<>(entityPage.getTotalCount(),entityPage.getResult().stream().map(objDo -> ObjectUtils.convertObject(objDo, TransactionDTO.class)).toList());
    }
}