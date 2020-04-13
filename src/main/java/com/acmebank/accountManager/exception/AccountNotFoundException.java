package com.acmebank.accountManager.exception;

import lombok.Getter;

public class AccountNotFoundException extends AccountManagerException {

    @Getter
    private String accountId;

    public AccountNotFoundException(String accountId) {
        super("Account not found: " + accountId);
        this.accountId = accountId;
    }
}
