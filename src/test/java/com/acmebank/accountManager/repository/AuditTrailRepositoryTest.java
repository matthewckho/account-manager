package com.acmebank.accountManager.repository;

import com.acmebank.accountManager.data.enums.AuditAction;
import com.acmebank.accountManager.entity.AuditTrailEntity;
import com.acmebank.accountManager.factory.TestDataFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AuditTrailRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuditTrailRepository auditTrailRepository;

    @Test
    public void testSaveAuditTrail() {
        AuditTrailEntity auditTrailEntity = TestDataFactory.generateAuditTrailEntity(AuditAction.TRANSFER_MONEY, "Transfer Money", LocalDateTime.now());
        auditTrailRepository.save(auditTrailEntity);
    }
}
