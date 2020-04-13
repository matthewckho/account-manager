package com.acmebank.accountManager.service;

import com.acmebank.accountManager.entity.AccountEntity;
import com.acmebank.accountManager.entity.AuditTrailEntity;
import com.acmebank.accountManager.exception.*;
import com.acmebank.accountManager.repository.AccountRepository;
import com.acmebank.accountManager.factory.TestDataFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransferMoneyServiceTest {

    @InjectMocks
    private TransferMoneyServiceImpl transferMoneyService;

    @Mock
    private AccountRepository accountRepository;

    @Captor
    private ArgumentCaptor<AccountEntity> accountEntityCaptor;

    @Test
    public void testTransferMoney() throws AccountManagerException {
        when(accountRepository.findByAccountId("12345678")).thenReturn(TestDataFactory.generateAccountEntity("12345678", "HKD", BigDecimal.valueOf(100)));
        when(accountRepository.findByAccountId("88888888")).thenReturn(TestDataFactory.generateAccountEntity("88888888", "HKD", BigDecimal.valueOf(500)));
        boolean result = transferMoneyService.transferMoney(TestDataFactory.generateTransferMoneyDTO("12345678", "88888888", "HKD", BigDecimal.valueOf(50)));

        assertTrue(result);
        verify(accountRepository, times(1)).findByAccountId("12345678");
        verify(accountRepository, times(1)).findByAccountId("88888888");
        verify(accountRepository, times(2)).save(accountEntityCaptor.capture());
        List<AccountEntity> saveAccountEntities = accountEntityCaptor.getAllValues();
        assertEquals("12345678", saveAccountEntities.get(0).getAccountId());
        assertEquals("HKD", saveAccountEntities.get(0).getCurrency());
        assertEquals(BigDecimal.valueOf(50), saveAccountEntities.get(0).getAmount());
        assertEquals("88888888", saveAccountEntities.get(1).getAccountId());
        assertEquals("HKD", saveAccountEntities.get(1).getCurrency());
        assertEquals(BigDecimal.valueOf(550), saveAccountEntities.get(1).getAmount());
    }

    @Test(expected = AccountNotFoundException.class)
    public void testTransferMoneyInvalidFromAccount() throws AccountManagerException {
        transferMoneyService.transferMoney(TestDataFactory.generateTransferMoneyDTO("11111", "22222", "HKD", BigDecimal.valueOf(50)));
    }

    @Test(expected = AccountNotFoundException.class)
    public void testTransferMoneyInvalidToAccount() throws AccountManagerException {
        when(accountRepository.findByAccountId("11111")).thenReturn(TestDataFactory.generateAccountEntity("11111", "HKD", BigDecimal.valueOf(500)));
        transferMoneyService.transferMoney(TestDataFactory.generateTransferMoneyDTO("11111", "22222", "HKD", BigDecimal.valueOf(50)));
    }

    @Test(expected = TransferBetweenSameAccountException.class)
    public void testTransferMoneySameAccount() throws AccountManagerException {
        transferMoneyService.transferMoney(TestDataFactory.generateTransferMoneyDTO("11111", "11111", "HKD", BigDecimal.valueOf(50)));
    }

    @Test(expected = InsufficientFundException.class)
    public void testTransferMoneyInsufficientFund() throws AccountManagerException {
        when(accountRepository.findByAccountId("11111")).thenReturn(TestDataFactory.generateAccountEntity("11111", "HKD", BigDecimal.valueOf(10)));
        when(accountRepository.findByAccountId("22222")).thenReturn(TestDataFactory.generateAccountEntity("22222", "HKD", BigDecimal.valueOf(100)));
        transferMoneyService.transferMoney(TestDataFactory.generateTransferMoneyDTO("11111", "22222", "HKD", BigDecimal.valueOf(50)));
    }

    @Test(expected = InvalidCurrencyException.class)
    public void testTransferMoneyInvalidCurrency() throws AccountManagerException {
        when(accountRepository.findByAccountId("11111")).thenReturn(TestDataFactory.generateAccountEntity("11111", "HKD", BigDecimal.valueOf(10)));
        when(accountRepository.findByAccountId("22222")).thenReturn(TestDataFactory.generateAccountEntity("22222", "HKD", BigDecimal.valueOf(100)));
        transferMoneyService.transferMoney(TestDataFactory.generateTransferMoneyDTO("11111", "22222", "JPY", BigDecimal.valueOf(50)));
    }
}
