package com.acmebank.accountManager.exception;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class InsufficientFundException extends AccountManagerException {

    private String currency;

    private BigDecimal amount;

    public InsufficientFundException(String currency, BigDecimal amount) {
        super("Insufficient transfer amount: " + currency + "$" + amount);
        this.currency = currency;
        this.amount = amount;
    }
}
