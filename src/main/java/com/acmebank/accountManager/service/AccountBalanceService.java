package com.acmebank.accountManager.service;

import com.acmebank.accountManager.data.dto.AmountDTO;
import com.acmebank.accountManager.exception.AccountManagerException;

public interface AccountBalanceService {

    AmountDTO getAccountBalance(String accountId) throws AccountManagerException;
}
