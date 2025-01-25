package com.lili.bank.client;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * domain model
 */
@lombok.Data
@NoArgsConstructor
public class ModifyTransactionDTO {
    @NotEmpty(message = "id cannot be empty")
    private String id;
    @NotEmpty(message = "accountId cannot be empty")
    private String accountId;
    @DecimalMin("0.01")
    private BigDecimal amount;

    public ModifyTransactionDTO(String id, String accountId, BigDecimal amount) {
        this.id = id;
        this.accountId=accountId;
        this.amount = amount;

    }
}
