package com.acmebank.accountManager.data.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AmountDTO {

    private String currency;

    private BigDecimal amount;
}
