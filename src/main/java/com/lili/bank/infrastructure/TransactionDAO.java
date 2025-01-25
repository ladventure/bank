package com.lili.bank.infrastructure;

import com.lili.bank.BankProperties;
import com.lili.bank.client.Page;
import com.lili.bank.common.exception.BankException;
import com.lili.bank.common.exception.BankExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.lili.bank.common.exception.BankExceptionEnum.RECORD_OVER_LIMIT_ERROR;

/**
 * use memory to mock database operate
 */
@Component
public class TransactionDAO {

    @Autowired
    BankProperties bankProperties;

    private final Map<String, TransactionDO> transactions = new ConcurrentHashMap<>();

    public TransactionDO save(TransactionDO transactionDO) {
        if (transactions.size()>=bankProperties.getMaxRecords()) {
            throw new BankException(RECORD_OVER_LIMIT_ERROR);
        }
        transactions.put(transactionDO.getId(), transactionDO);
        return transactionDO;
    }

    public Optional<TransactionDO> findById(String id) {
        return Optional.ofNullable(transactions.get(id));
    }

    public Page<TransactionDO> find(TransactionDO transactionDO, int pageNumber, int pageSize) {
        List<TransactionDO> dos = new ArrayList<>(transactions.values().stream()
                .filter(v -> {
                    if (transactionDO == null || transactionDO.getId() == null || transactionDO.getId().equals(v.getId())) {
                        return true;
                    }
                    return false;
                })
                .filter(v -> {
                    if (transactionDO == null || transactionDO.getType() == null || transactionDO.getType().equals(v.getType())) {
                        return true;
                    }
                    return false;
                })
                .filter(v -> {
                    if (transactionDO == null || transactionDO.getAmount() == null || transactionDO.getAmount().equals(v.getAmount())) {
                        return true;
                    }
                    return false;
                })
                .filter(v -> {
                    if (transactionDO == null || transactionDO.getAccountId() == null || transactionDO.getAccountId().equals(v.getAccountId())) {
                        return true;
                    }
                    return false;
                })
                .toList());
        dos.sort(Comparator.comparing(TransactionDO::getId));
        return new Page<>(dos.size(), dos.subList((pageNumber - 1) * pageSize, Math.min(dos.size(), pageNumber * pageSize)));

    }

    public void deleteById(String id) {
        transactions.remove(id);
    }

    public TransactionDO update(TransactionDO transactionDO) {
        transactions.put(transactionDO.getId(), transactionDO);
        return transactionDO;
    }
}