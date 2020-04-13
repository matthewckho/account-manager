package com.acmebank.accountManager.error;

import com.acmebank.accountManager.controller.AccountManagerController;
import com.acmebank.accountManager.data.request.TransferMoneyRequest;
import com.acmebank.accountManager.exception.*;
import com.acmebank.accountManager.factory.TestDataFactory;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.LockTimeoutException;
import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
public class ErrorHandlerTest {

    @MockBean
    private AccountManagerController accountManagerController;

    private MockMvc mockMvc;

    private Gson gson;

    @Before
    public void initialize() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountManagerController).setControllerAdvice(new ErrorHandler()).build();
        gson = new Gson();
    }

    @Test
    public void testGetBalanceAccountNotFound() throws Exception {
        String accountId = "12345";
        when(accountManagerController.getAccountBalance(anyString())).thenThrow(new AccountNotFoundException(accountId));
        mockMvc.perform(get("/v0/account-manager/getAccountBalance").param("accountId", accountId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.toString())))
                .andExpect(jsonPath("$.message", is("Account not found: " + accountId)));
    }

    @Test
    public void testTransferMoneyAccountNotFound() throws Exception {
        TransferMoneyRequest request = TestDataFactory.generateTransferMoneyRequest("11111", "22222", "HKD", BigDecimal.valueOf(100));
        when(accountManagerController.transferMoney(any(TransferMoneyRequest.class))).thenThrow(new AccountNotFoundException(request.getFromAccountId()));
        mockMvc.perform(post("/v0/account-manager/transferMoney").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.toString())))
                .andExpect(jsonPath("$.message", is("Account not found: " + request.getFromAccountId())));
    }

    @Test
    public void testTransferMoneyTransferBetweenSameAccount() throws Exception {
        TransferMoneyRequest request = TestDataFactory.generateTransferMoneyRequest("11111", "11111", "HKD", BigDecimal.valueOf(100));
        when(accountManagerController.transferMoney(any(TransferMoneyRequest.class))).thenThrow(new TransferBetweenSameAccountException(request.getFromAccountId()));
        mockMvc.perform(post("/v0/account-manager/transferMoney").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())))
                .andExpect(jsonPath("$.message", is("Transfer between same account not allowed: " + request.getFromAccountId())));
    }

    @Test
    public void testTransferMoneyInsufficientFund() throws Exception {
        TransferMoneyRequest request = TestDataFactory.generateTransferMoneyRequest("11111", "22222", "HKD", BigDecimal.valueOf(10000));
        when(accountManagerController.transferMoney(any(TransferMoneyRequest.class))).thenThrow(new InsufficientFundException(request.getCurrency(), request.getAmount()));
        mockMvc.perform(post("/v0/account-manager/transferMoney").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())))
                .andExpect(jsonPath("$.message", is("Insufficient transfer amount: " + request.getCurrency() + "$" + request.getAmount())));
    }

    @Test
    public void testTransferMoneyInvalidCurrency() throws Exception {
        TransferMoneyRequest request = TestDataFactory.generateTransferMoneyRequest("11111", "22222", "JYP", BigDecimal.valueOf(100));
        when(accountManagerController.transferMoney(any(TransferMoneyRequest.class))).thenThrow(new InvalidCurrencyException(request.getCurrency()));
        mockMvc.perform(post("/v0/account-manager/transferMoney").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())))
                .andExpect(jsonPath("$.message", is("Invalid transfer currency: " + request.getCurrency())));
    }

    @Test
    public void testFailToObtainLock() throws Exception {
        TransferMoneyRequest request = TestDataFactory.generateTransferMoneyRequest("11111", "22222", "HKD", BigDecimal.valueOf(100));
        when(accountManagerController.transferMoney(any(TransferMoneyRequest.class))).thenThrow(new LockTimeoutException("Fail to lock account record"));
        mockMvc.perform(post("/v0/account-manager/transferMoney").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status", is(HttpStatus.FORBIDDEN.toString())))
                .andExpect(jsonPath("$.message", is("Fail to lock account record")));
    }
}
