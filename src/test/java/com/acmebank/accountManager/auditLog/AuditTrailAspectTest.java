package com.acmebank.accountManager.auditLog;

import com.acmebank.accountManager.data.dto.TransferMoneyDTO;
import com.acmebank.accountManager.data.enums.AuditAction;
import com.acmebank.accountManager.entity.AuditTrailEntity;
import com.acmebank.accountManager.exception.AccountManagerException;
import com.acmebank.accountManager.exception.AccountNotFoundException;
import com.acmebank.accountManager.factory.TestDataFactory;
import com.acmebank.accountManager.repository.AuditTrailRepository;
import com.acmebank.accountManager.service.AccountBalanceServiceImpl;
import com.acmebank.accountManager.service.TransferMoneyServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuditTrailAspectTest {

    @Autowired
    private TransferMoneyServiceImpl transferMoneyService;

    @Autowired
    private AccountBalanceServiceImpl accountBalanceService;

    @MockBean
    private AuditTrailRepository auditTrailRepository;

    @Captor
    private ArgumentCaptor<AuditTrailEntity> auditTrailEntityCaptor;

    @Test
    public void testTransferMoneyAuditTrail() throws AccountManagerException {
        TransferMoneyDTO transferMoneyDTO = TestDataFactory.generateTransferMoneyDTO("12345678", "88888888", "HKD", BigDecimal.valueOf(1000));
        transferMoneyService.transferMoney(transferMoneyDTO);

        verify(auditTrailRepository, times(1)).save(auditTrailEntityCaptor.capture());

        AuditTrailEntity saveAuditTrailEntity = auditTrailEntityCaptor.getValue();
        assertEquals(AuditAction.TRANSFER_MONEY, saveAuditTrailEntity.getAction());
        assertEquals(String.format("Transfer %s $%s from account ID %s to account ID %s", transferMoneyDTO.getCurrency(), transferMoneyDTO.getAmount(), transferMoneyDTO.getFromAccountId(), transferMoneyDTO.getToAccountId()), saveAuditTrailEntity.getMessage());
    }

    @Test(expected = AccountManagerException.class)
    public void testFailTransferMoneyAuditTrail() throws AccountManagerException {
        TransferMoneyDTO transferMoneyDTO = TestDataFactory.generateTransferMoneyDTO("123", "88888888", "HKD", BigDecimal.valueOf(1000));
        try {
            transferMoneyService.transferMoney(transferMoneyDTO);
        } catch (Exception ex) {
            assertTrue(ex instanceof AccountManagerException);
            verify(auditTrailRepository, times(1)).save(auditTrailEntityCaptor.capture());
            AuditTrailEntity saveAuditTrailEntity = auditTrailEntityCaptor.getValue();
            assertEquals(AuditAction.TRANSFER_MONEY, saveAuditTrailEntity.getAction());
            assertEquals(String.format("Fail to transfer %s $%s from account ID %s to account ID %s - %s", transferMoneyDTO.getCurrency(), transferMoneyDTO.getAmount(), transferMoneyDTO.getFromAccountId(), transferMoneyDTO.getToAccountId(), ex.getMessage()), saveAuditTrailEntity.getMessage());
            throw ex;
        }
    }

    @Test
    public void testGetAccountBalanceAuditTrail() throws AccountManagerException {
        String accountId = "12345678";
        accountBalanceService.getAccountBalance(accountId);

        verify(auditTrailRepository, times(1)).save(auditTrailEntityCaptor.capture());
        AuditTrailEntity saveAuditTrailEntity = auditTrailEntityCaptor.getValue();
        assertEquals(AuditAction.GET_ACCOUNT_BALANCE, saveAuditTrailEntity.getAction());
        assertEquals(String.format("Get balance of account ID %s", accountId), saveAuditTrailEntity.getMessage());
    }

    @Test(expected = AccountNotFoundException.class)
    public void testFailGetAccountBalanceAuditTrail() throws AccountManagerException {
        String accountId = "11111";
        try {
            accountBalanceService.getAccountBalance(accountId);
        } catch (Exception ex) {
            assertTrue(ex instanceof AccountNotFoundException);
            verify(auditTrailRepository, times(1)).save(auditTrailEntityCaptor.capture());
            AuditTrailEntity saveAuditTrailEntity = auditTrailEntityCaptor.getValue();
            assertEquals(AuditAction.GET_ACCOUNT_BALANCE, saveAuditTrailEntity.getAction());
            assertEquals(String.format("Fail to get balance of account ID %s", accountId), saveAuditTrailEntity.getMessage());
            throw ex;
        }
    }
}
