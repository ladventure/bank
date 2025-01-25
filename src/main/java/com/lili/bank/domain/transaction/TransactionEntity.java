package com.lili.bank.domain.transaction;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * domain model
 */
@lombok.Data
@NoArgsConstructor
public class TransactionEntity {
    private String id;
    private String accountId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String type;

    public TransactionEntity(String id,String accountId, BigDecimal amount, LocalDateTime timestamp, String type) {
        this.id = id;
        this.accountId=accountId;
        this.amount = amount;
        this.timestamp = timestamp;
        this.type = type;
    }
}
