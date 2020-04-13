package com.acmebank.accountManager.repository;

import com.acmebank.accountManager.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity,String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    AccountEntity findByAccountId(String accountId);
}
