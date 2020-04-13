package com.acmebank.accountManager.service;

import com.acmebank.accountManager.data.dto.TransferMoneyDTO;
import com.acmebank.accountManager.exception.AccountManagerException;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

public interface TransferMoneyService {

    @Transactional
    boolean transferMoney(TransferMoneyDTO transferMoney) throws AccountManagerException, PersistenceException;
}
