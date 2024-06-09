package com.harrishjoshi.springaop.audit.trails.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuditEventLogRepository extends JpaRepository<AuditEventLog, Integer> {


    @Query(name = "AuditEventLog.findAllBy", nativeQuery = true)
    List<AuditEventLogResponse> findAllBy();

    @Query(nativeQuery = true, name = "AuditEventLog.getAuditEventLogByUserIdOrEntityId")
    List<AuditEventLogResponse> getAuditEventLogByUserIdOrEntityId(Integer userIdOrEntityId);
}
