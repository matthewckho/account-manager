package com.acmebank.accountManager.controller;

import com.acmebank.accountManager.adapter.AccountManagerAdapter;
import com.acmebank.accountManager.data.response.AmountResponse;
import com.acmebank.accountManager.data.request.TransferMoneyRequest;
import com.acmebank.accountManager.exception.AccountManagerException;
import com.acmebank.accountManager.service.AccountBalanceService;
import com.acmebank.accountManager.service.TransferMoneyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/v0/account-manager", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountManagerController {

    @Autowired
    private AccountBalanceService accountBalanceService;

    @Autowired
    private TransferMoneyService transferMoneyService;

    @GetMapping("/getAccountBalance")
    public AmountResponse getAccountBalance(@RequestParam("accountId") String accountId) throws AccountManagerException {
        log.info("Request for account balance for account {}", accountId);
        return AccountManagerAdapter.toAmountResponse(accountBalanceService.getAccountBalance(accountId));
    }

    @PostMapping("/transferMoney")
    public boolean transferMoney(@Valid @RequestBody TransferMoneyRequest request) throws AccountManagerException {
        log.info("Request to transfer money {}", request);
        boolean result = transferMoneyService.transferMoney(AccountManagerAdapter.toTransferMoneyDTO(request));
        log.info("Transfer completed");

        return result;
    }
}
