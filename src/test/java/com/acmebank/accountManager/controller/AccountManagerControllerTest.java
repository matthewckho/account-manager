package com.acmebank.accountManager.controller;

import com.acmebank.accountManager.data.dto.AmountDTO;
import com.acmebank.accountManager.data.dto.TransferMoneyDTO;
import com.acmebank.accountManager.data.request.TransferMoneyRequest;
import com.acmebank.accountManager.factory.TestDataFactory;
import com.acmebank.accountManager.service.AccountBalanceService;
import com.acmebank.accountManager.service.TransferMoneyService;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountManagerController.class)
public class AccountManagerControllerTest {

    @MockBean
    private AccountBalanceService accountBalanceService;

    @MockBean
    private TransferMoneyService transferMoneyService;

    @Autowired
    private MockMvc mockMvc;

    private Gson gson;

    @Before
    public void initialize() {
        gson = new Gson();
    }

    @Test
    public void testGetBalance() throws Exception {
        String accountId = "12345";
        AmountDTO amountDTO = TestDataFactory.generateAmountDTO("HKD", BigDecimal.valueOf(5000));
        when(accountBalanceService.getAccountBalance(accountId)).thenReturn(amountDTO);
        mockMvc.perform(get("/v0/account-manager/getAccountBalance").param("accountId", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency", is(amountDTO.getCurrency())))
                .andExpect(jsonPath("$.amount", is(5000)));

        verify(accountBalanceService, times(1)).getAccountBalance(accountId);
    }

    @Test
    public void testTransferMoney() throws Exception {
        TransferMoneyRequest request = TestDataFactory.generateTransferMoneyRequest("11111", "22222", "HKD", BigDecimal.valueOf(100));
        when(transferMoneyService.transferMoney(any(TransferMoneyDTO.class))).thenReturn(true);
        mockMvc.perform(post("/v0/account-manager/transferMoney").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(Boolean.TRUE.toString()));

        verify(transferMoneyService, times(1)).transferMoney(any(TransferMoneyDTO.class));
    }

    @Test
    public void testTransferMoneyMissingFromAccount() throws Exception {
        TransferMoneyRequest request = TestDataFactory.generateTransferMoneyRequest(null, "22222", "HKD", BigDecimal.valueOf(100));
        mockMvc.perform(post("/v0/account-manager/transferMoney").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(request)))
                .andExpect(status().isBadRequest());

        verify(transferMoneyService, times(0)).transferMoney(any(TransferMoneyDTO.class));
    }

    @Test
    public void testTransferMoneyMissingToAccount() throws Exception {
        TransferMoneyRequest request = TestDataFactory.generateTransferMoneyRequest("11111", null, "HKD", BigDecimal.valueOf(100));
        mockMvc.perform(post("/v0/account-manager/transferMoney").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(request)))
                .andExpect(status().isBadRequest());

        verify(transferMoneyService, times(0)).transferMoney(any(TransferMoneyDTO.class));
    }

    @Test
    public void testTransferInvalidCurrency() throws Exception {
        TransferMoneyRequest request = TestDataFactory.generateTransferMoneyRequest("11111", "222222", "Hong Kong Dollar", BigDecimal.valueOf(100));
        mockMvc.perform(post("/v0/account-manager/transferMoney").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(request)))
                .andExpect(status().isBadRequest());

        verify(transferMoneyService, times(0)).transferMoney(any(TransferMoneyDTO.class));
    }

    @Test
    public void testTransferInvalidAmount() throws Exception {
        TransferMoneyRequest request = TestDataFactory.generateTransferMoneyRequest("11111", "222222", "HKD", BigDecimal.valueOf(100.001));
        mockMvc.perform(post("/v0/account-manager/transferMoney").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(request)))
                .andExpect(status().isBadRequest());

        verify(transferMoneyService, times(0)).transferMoney(any(TransferMoneyDTO.class));
    }
}
