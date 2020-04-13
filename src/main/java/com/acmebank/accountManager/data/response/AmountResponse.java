package com.acmebank.accountManager.data.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AmountResponse {

    private String currency;

    private BigDecimal amount;
}
