package com.acmebank.accountManager.factory;

import com.acmebank.accountManager.data.dto.AmountDTO;
import com.acmebank.accountManager.data.dto.TransferMoneyDTO;
import com.acmebank.accountManager.data.enums.AuditAction;
import com.acmebank.accountManager.data.request.TransferMoneyRequest;
import com.acmebank.accountManager.entity.AccountEntity;
import com.acmebank.accountManager.entity.AuditTrailEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestDataFactory {

    public static AccountEntity generateAccountEntity(String accountId, String currency, BigDecimal amount) {
        AccountEntity entity = new AccountEntity();
        entity.setAccountId(accountId);
        entity.setCurrency(currency);
        entity.setAmount(amount);
        return entity;
    }

    public static TransferMoneyDTO generateTransferMoneyDTO(String fromAccountId, String toAccountId, String currency, BigDecimal amount) {
        TransferMoneyDTO dto = new TransferMoneyDTO();
        dto.setFromAccountId(fromAccountId);
        dto.setToAccountId(toAccountId);
        dto.setCurrency(currency);
        dto.setAmount(amount);
        return dto;
    }

    public static TransferMoneyRequest generateTransferMoneyRequest(String fromAccountId, String toAccountId, String currency, BigDecimal amount) {
        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setFromAccountId(fromAccountId);
        transferMoneyRequest.setToAccountId(toAccountId);
        transferMoneyRequest.setCurrency(currency);
        transferMoneyRequest.setAmount(amount);
        return transferMoneyRequest;
    }

    public static AmountDTO generateAmountDTO(String currency, BigDecimal amount) {
        AmountDTO amountDto = new AmountDTO();
        amountDto.setCurrency(currency);
        amountDto.setAmount(amount);
        return amountDto;
    }

    public static AuditTrailEntity generateAuditTrailEntity(AuditAction action, String message, LocalDateTime auditTime) {
        AuditTrailEntity auditTrailEntity = new AuditTrailEntity();
        auditTrailEntity.setAction(action);
        auditTrailEntity.setMessage(message);
        auditTrailEntity.setAuditTime(auditTime);
        return auditTrailEntity;
    }
}
