package com.acmebank.accountManager.repository;

import com.acmebank.accountManager.entity.AuditTrailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditTrailRepository extends JpaRepository<AuditTrailEntity, Long> {
}
