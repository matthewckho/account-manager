package com.acmebank.accountManager.exception;

import lombok.Getter;

@Getter
public class InvalidCurrencyException extends AccountManagerException {

    private String currency;

    public InvalidCurrencyException(String currency) {
        super("Invalid transfer currency: " + currency);
        this.currency = currency;
    }
}
