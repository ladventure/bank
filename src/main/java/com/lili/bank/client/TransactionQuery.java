package com.lili.bank.client;

import jakarta.validation.constraints.DecimalMin;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * domain model
 */
@lombok.Data
@NoArgsConstructor
public class TransactionQuery extends PageRequest {
    private String id;
    private String accountId;
    @DecimalMin("0.01")
    private BigDecimal amount;
    private String type;

    public TransactionQuery(String id, String accountId, BigDecimal amount, LocalDateTime timestamp, String type) {
        this.id = id;
        this.accountId=accountId;
        this.amount = amount;
        this.type = type;
    }
}
