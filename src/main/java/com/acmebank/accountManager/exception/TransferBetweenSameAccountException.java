package com.acmebank.accountManager.exception;

import lombok.Getter;

public class TransferBetweenSameAccountException extends AccountManagerException {

    @Getter
    private String accountId;

    public TransferBetweenSameAccountException(String accountId) {
        super("Transfer between same account not allowed: " + accountId);
        this.accountId = accountId;
    }
}
