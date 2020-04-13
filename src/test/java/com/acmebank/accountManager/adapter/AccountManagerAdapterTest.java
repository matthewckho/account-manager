package com.acmebank.accountManager.adapter;

import com.acmebank.accountManager.data.dto.AmountDTO;
import com.acmebank.accountManager.data.dto.TransferMoneyDTO;
import com.acmebank.accountManager.data.request.TransferMoneyRequest;
import com.acmebank.accountManager.data.response.AmountResponse;
import com.acmebank.accountManager.entity.AccountEntity;
import com.acmebank.accountManager.factory.TestDataFactory;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class AccountManagerAdapterTest {

    @Test
    public void testToTransferMoneyDTO() {
        TransferMoneyRequest transferMoneyRequest = TestDataFactory.generateTransferMoneyRequest("11111", "22222", "HKD", BigDecimal.valueOf(700));
        TransferMoneyDTO transferMoneyDTO = AccountManagerAdapter.toTransferMoneyDTO(transferMoneyRequest);

        assertEquals(transferMoneyRequest.getFromAccountId(), transferMoneyDTO.getFromAccountId());
        assertEquals(transferMoneyRequest.getToAccountId(), transferMoneyDTO.getToAccountId());
        assertEquals(transferMoneyRequest.getCurrency(), transferMoneyDTO.getCurrency());
        assertEquals(transferMoneyRequest.getAmount(), transferMoneyDTO.getAmount());
    }

    @Test
    public void testToAmountDTO() {
        AccountEntity accountEntity = TestDataFactory.generateAccountEntity("12345", "HKD", BigDecimal.valueOf(500));
        AmountDTO amountDto = AccountManagerAdapter.toAmountDTO(accountEntity);

        assertEquals(accountEntity.getCurrency(), amountDto.getCurrency());
        assertEquals(accountEntity.getAmount(), amountDto.getAmount());
    }

    @Test
    public void testToAmountResponse() {
        AmountDTO amountDto = TestDataFactory.generateAmountDTO("HKD", BigDecimal.valueOf(500));
        AmountResponse amountResponse = AccountManagerAdapter.toAmountResponse(amountDto);

        assertEquals(amountDto.getCurrency(), amountResponse.getCurrency());
        assertEquals(amountDto.getAmount(), amountResponse.getAmount());
    }
}
