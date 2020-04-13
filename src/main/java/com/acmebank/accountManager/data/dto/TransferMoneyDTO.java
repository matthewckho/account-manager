package com.acmebank.accountManager.data.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferMoneyDTO {

    private String fromAccountId;

    private String toAccountId;

    private String currency;

    private BigDecimal amount;
}
