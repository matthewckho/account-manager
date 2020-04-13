package com.acmebank.accountManager.repository;

import com.acmebank.accountManager.entity.AccountEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void testFindByAccountId() {
        AccountEntity accountEntity = accountRepository.findByAccountId("12345678");

        assertNotNull(accountEntity);
        assertEquals("12345678", accountEntity.getAccountId());
        assertEquals("HKD", accountEntity.getCurrency());
        assertEquals(BigDecimal.valueOf(1000000).setScale(2, 2), accountEntity.getAmount());
    }

    @Test
    public void testFindByAccountIdNotFound() {
        AccountEntity accountEntity = accountRepository.findByAccountId("123456788");

        assertNull(accountEntity);
    }

    @Test
    public void testFindById() {
        Optional<AccountEntity> optional = accountRepository.findById("12345678");

        assertTrue(optional.isPresent());
        AccountEntity accountEntity = optional.get();
        assertEquals("12345678", accountEntity.getAccountId());
        assertEquals("HKD", accountEntity.getCurrency());
        assertEquals(BigDecimal.valueOf(1000000).setScale(2), accountEntity.getAmount());
    }

    @Test
    public void testFindByIdNotFound() {
        Optional<AccountEntity> optional = accountRepository.findById("123456788");

        assertFalse(optional.isPresent());
    }

    @Test
    public void updateAccountAmount() {
        AccountEntity accountEntity = accountRepository.findByAccountId("12345678");
        accountEntity.setAmount(BigDecimal.valueOf(9000));
        accountRepository.save(accountEntity);
        AccountEntity updatedAccountEntity = accountRepository.findByAccountId("12345678");

        assertNotNull(updatedAccountEntity);
        assertEquals("12345678", updatedAccountEntity.getAccountId());
        assertEquals("HKD", updatedAccountEntity.getCurrency());
        assertEquals(BigDecimal.valueOf(9000), updatedAccountEntity.getAmount());
    }
}
