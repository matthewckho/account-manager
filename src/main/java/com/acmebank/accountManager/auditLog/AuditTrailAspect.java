package com.acmebank.accountManager.auditLog;

import com.acmebank.accountManager.data.dto.TransferMoneyDTO;
import com.acmebank.accountManager.data.enums.AuditAction;
import com.acmebank.accountManager.entity.AuditTrailEntity;
import com.acmebank.accountManager.exception.AccountNotFoundException;
import com.acmebank.accountManager.repository.AuditTrailRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Aspect
@Component
public class AuditTrailAspect {

    @Autowired
    private AuditTrailRepository auditTrailRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @AfterReturning(pointcut = "execution(* com.acmebank.accountManager.service.TransferMoneyServiceImpl.transferMoney(..))")
    public void transferMoneyAuditTrail(JoinPoint joinPoint) {
        TransferMoneyDTO transferMoney = (TransferMoneyDTO)joinPoint.getArgs()[0];
        saveAuditTrail(AuditAction.TRANSFER_MONEY, String.format("Transfer %s $%s from account ID %s to account ID %s", transferMoney.getCurrency(), transferMoney.getAmount(), transferMoney.getFromAccountId(), transferMoney.getToAccountId()));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @AfterThrowing(pointcut = "execution(* com.acmebank.accountManager.service.TransferMoneyServiceImpl.transferMoney(..))", throwing = "ex")
    public void failTransferMoneyAuditTrail(JoinPoint joinPoint, Exception ex) {
        TransferMoneyDTO transferMoney = (TransferMoneyDTO)joinPoint.getArgs()[0];
        saveAuditTrail(AuditAction.TRANSFER_MONEY, String.format("Fail to transfer %s $%s from account ID %s to account ID %s - %s", transferMoney.getCurrency(), transferMoney.getAmount(), transferMoney.getFromAccountId(), transferMoney.getToAccountId(), ex.getMessage()));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @AfterReturning(pointcut = "execution(* com.acmebank.accountManager.service.AccountBalanceServiceImpl.getAccountBalance(..))")
    public void getAccountBalanceAuditTrail(JoinPoint joinPoint) {
        saveAuditTrail(AuditAction.GET_ACCOUNT_BALANCE, String.format("Get balance of account ID %s", (String)joinPoint.getArgs()[0]));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @AfterThrowing(pointcut = "execution(* com.acmebank.accountManager.service.AccountBalanceServiceImpl.getAccountBalance(..))", throwing = "ex")
    public void failGetAccountBalanceAuditTrail(JoinPoint joinPoint, AccountNotFoundException ex) {
        saveAuditTrail(AuditAction.GET_ACCOUNT_BALANCE, String.format("Fail to get balance of account ID %s", (String)joinPoint.getArgs()[0]));
    }

    private void saveAuditTrail(AuditAction action, String message) {
        AuditTrailEntity auditTrailEntity = new AuditTrailEntity();
        auditTrailEntity.setAction(action);
        auditTrailEntity.setMessage(message);
        auditTrailEntity.setAuditTime(LocalDateTime.now());
        auditTrailRepository.save(auditTrailEntity);
    }
}
