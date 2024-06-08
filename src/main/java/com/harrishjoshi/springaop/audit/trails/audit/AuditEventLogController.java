package com.harrishjoshi.springaop.audit.trails.audit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit/event-log")
public class AuditEventLogController {

    private final AuditEventLogService auditEventLogService;

    public AuditEventLogController(AuditEventLogService auditEventLogService) {
        this.auditEventLogService = auditEventLogService;
    }

    @GetMapping
    public ResponseEntity<List<AuditEventLogResponse>> getAuditEventLog() {
        return ResponseEntity.ok(auditEventLogService.getAuditEventLog());
    }
}
