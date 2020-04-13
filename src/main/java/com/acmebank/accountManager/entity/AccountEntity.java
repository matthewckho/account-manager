package com.acmebank.accountManager.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "ACCOUNT")
public class AccountEntity implements Serializable {

    private static final long serialVersionUID = -930537270212105908L;

    @Id
    @Column(name = "ACCOUNT_ID", unique = true, nullable = false, updatable = false)
    private String accountId;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "AMOUNT", precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "LAST_UPDATED_TIME", columnDefinition = "TIMESTAMP")
    private LocalDateTime lastUpdatedTime;
}
