package com.lili.bank.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * domain model
 */
@lombok.Data
@NoArgsConstructor
public class TransactionDTO {
    private String id;
    @NotBlank(message = "accountId cannot be blank")
    private String accountId;
    @DecimalMin("0.01")
    private BigDecimal amount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    @NotBlank(message = "type cannot be blank")
    @Pattern(regexp = "DEPOSIT|WITHDRAWAL",message = "type must be DEPOSIT OR WITHDRAWAL")
    private String type;

    public TransactionDTO(String id,String accountId, BigDecimal amount, LocalDateTime timestamp, String type) {
        this.id = id;
        this.accountId=accountId;
        this.amount = amount;
        this.timestamp = timestamp;
        this.type = type;
    }
}
