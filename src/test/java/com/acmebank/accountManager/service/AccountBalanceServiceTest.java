package com.acmebank.accountManager.service;

import com.acmebank.accountManager.data.dto.AmountDTO;
import com.acmebank.accountManager.exception.AccountManagerException;
import com.acmebank.accountManager.exception.AccountNotFoundException;
import com.acmebank.accountManager.repository.AccountRepository;
import com.acmebank.accountManager.factory.TestDataFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountBalanceServiceTest {

    @InjectMocks
    private AccountBalanceServiceImpl accountBalanceService;

    @Mock
    private AccountRepository accountRepository;

    @Test(expected = AccountNotFoundException.class)
    public void testGetBalanceAccountNotFound() throws AccountManagerException {
        when(accountRepository.findById("12345")).thenReturn(Optional.empty());
        accountBalanceService.getAccountBalance("12345");
    }

    @Test
    public void testGetBalance() throws AccountManagerException {
        when(accountRepository.findById("12345")).thenReturn(Optional.of(TestDataFactory.generateAccountEntity("12345", "HKD", BigDecimal.valueOf(500))));

        AmountDTO amount = accountBalanceService.getAccountBalance("12345");

        assertEquals(BigDecimal.valueOf(500), amount.getAmount());
    }
}
