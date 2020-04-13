package com.acmebank.accountManager.data.request;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class TransferMoneyRequest {

    @NotNull(message = "Missing from Account ID")
    private String fromAccountId;

    @NotNull(message = "Missing to Account ID")
    private String toAccountId;

    @NotNull(message = "Invalid currency")
    @Size(min = 3, max = 3)
    private String currency;

    @DecimalMin(value = "0", inclusive = false, message = "Transfer amount must be positive")
    @Digits(integer = 13, fraction = 2, message = "Invalid amount")
    private BigDecimal amount;
}
