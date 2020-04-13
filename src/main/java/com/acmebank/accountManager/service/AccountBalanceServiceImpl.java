package com.acmebank.accountManager.service;

import com.acmebank.accountManager.adapter.AccountManagerAdapter;
import com.acmebank.accountManager.data.dto.AmountDTO;
import com.acmebank.accountManager.entity.AccountEntity;
import com.acmebank.accountManager.exception.AccountManagerException;
import com.acmebank.accountManager.exception.AccountNotFoundException;
import com.acmebank.accountManager.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AccountBalanceServiceImpl implements AccountBalanceService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public AmountDTO getAccountBalance(String accountId) throws AccountManagerException {
        Optional<AccountEntity> optional = accountRepository.findById(accountId.trim());

        if (!optional.isPresent()) {
            handleAccountNotFound(accountId);
        }
        return AccountManagerAdapter.toAmountDTO(optional.get());
    }

    private void handleAccountNotFound(String accountId) throws AccountNotFoundException {
        log.info("Account not found: {}", accountId);
        throw new AccountNotFoundException(accountId);
    }
}
