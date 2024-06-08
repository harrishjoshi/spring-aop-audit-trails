package com.harrishjoshi.springaop.audit.trails.audit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AuditEventLogService {

    private final AuditEventLogRepository auditEventLogRepository;

    public AuditEventLogService(AuditEventLogRepository auditEventLogRepository) {
        this.auditEventLogRepository = auditEventLogRepository;
    }

    public void saveEventLog(EventLogRequest eventLogRequest) {
        log.info("EventLogRequest: {}", eventLogRequest);
        var auditEventLog = mapRequestToAuditEventLogEntity(eventLogRequest);
        auditEventLogRepository.save(auditEventLog);
    }


    AuditEventLog mapRequestToAuditEventLogEntity(EventLogRequest eventLogRequest) {
        var auditEventLog = new AuditEventLog();
        BeanUtils.copyProperties(eventLogRequest, auditEventLog);
        if (eventLogRequest.getJsonDiff() != null) {
            auditEventLog.setJsonDiff(eventLogRequest.getJsonDiff().toString());
        }

        return auditEventLog;
    }

    public List<AuditEventLogResponse> getAuditEventLog() {
        return auditEventLogRepository.findAllBy();
    }
}
