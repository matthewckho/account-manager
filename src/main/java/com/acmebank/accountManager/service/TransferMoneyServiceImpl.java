package com.acmebank.accountManager.service;

import com.acmebank.accountManager.data.dto.TransferMoneyDTO;
import com.acmebank.accountManager.entity.AccountEntity;
import com.acmebank.accountManager.exception.*;
import com.acmebank.accountManager.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.math.BigDecimal;

@Slf4j
@Service
public class TransferMoneyServiceImpl implements TransferMoneyService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    @Transactional
    public boolean transferMoney(TransferMoneyDTO transferMoney) throws AccountManagerException, PersistenceException {
        if (StringUtils.equals(transferMoney.getFromAccountId().trim(), transferMoney.getToAccountId().trim())) {
            handleTransferSameAccount(transferMoney.getFromAccountId());
        }
        AccountEntity fromAccount = accountRepository.findByAccountId(transferMoney.getFromAccountId().trim());
        AccountEntity toAccount = accountRepository.findByAccountId(transferMoney.getToAccountId().trim());
        validateTransferMoney(fromAccount, toAccount, transferMoney);
        fromAccount.setAmount(fromAccount.getAmount().subtract(transferMoney.getAmount()));
        toAccount.setAmount(toAccount.getAmount().add(transferMoney.getAmount()));
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        return true;
    }

    private void validateTransferMoney(AccountEntity fromAccount, AccountEntity toAccount, TransferMoneyDTO transferMoney) throws AccountManagerException {
        if (fromAccount == null) {
            handleAccountNotFound(transferMoney.getFromAccountId());
        }
        if (toAccount == null) {
            handleAccountNotFound(transferMoney.getToAccountId());
        }
        if (!StringUtils.equals(fromAccount.getCurrency(), transferMoney.getCurrency())
                || !StringUtils.equals(toAccount.getCurrency(), transferMoney.getCurrency()) ) {
            handleInvalidCurrency(transferMoney.getCurrency());
        }
        if (fromAccount.getAmount().compareTo(transferMoney.getAmount()) == -1) {
            handleInsufficientFund(transferMoney.getCurrency(), transferMoney.getAmount());
        }
    }

    private void handleTransferSameAccount(String accountId) throws TransferBetweenSameAccountException {
        log.info("Cannot transfer between same account {}", accountId);
        throw new TransferBetweenSameAccountException(accountId);
    }

    private void handleAccountNotFound(String accountId) throws AccountNotFoundException {
        log.info("Account not found: {}", accountId);
        throw new AccountNotFoundException(accountId);
    }

    private void handleInsufficientFund(String currency, BigDecimal amount) throws InsufficientFundException {
        log.info("Insufficient transfer amount: {}${}", currency, amount);
        throw new InsufficientFundException(currency, amount);
    }

    private void handleInvalidCurrency(String currency) throws InvalidCurrencyException {
        log.info("Invalid transfer currency: {}", currency);
        throw new InvalidCurrencyException(currency);
    }
}
