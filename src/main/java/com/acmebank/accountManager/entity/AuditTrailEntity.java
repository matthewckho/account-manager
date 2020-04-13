package com.acmebank.accountManager.entity;

import com.acmebank.accountManager.data.enums.AuditAction;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "AUDIT_TRAIL")
public class AuditTrailEntity implements Serializable {

    private static final long serialVersionUID = 7070054237484701868L;

    @Id
    @GeneratedValue
    @Column(name = "AUDIT_TRAIL_OID", unique = true, nullable = false, updatable = false, length = 10)
    private Long id;

    @Column(name = "ACTION", updatable = false, nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private AuditAction action;

    @Column(name = "MESSAGE", updatable = false)
    private String message;

    @Column(name = "AUDIT_TIME", columnDefinition = "TIMESTAMP", updatable = false, nullable = false)
    private LocalDateTime auditTime;
}
