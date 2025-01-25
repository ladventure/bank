package com.lili.bank.infrastructure;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * database model
 */
@lombok.Data
@NoArgsConstructor
public class TransactionDO {
    private String id;
    private String accountId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String type;

    public TransactionDO(String id,String accountId, BigDecimal amount, LocalDateTime timestamp, String type) {
        this.id = id;
        this.accountId=accountId;
        this.amount = amount;
        this.timestamp = timestamp;
        this.type = type;
    }
}
