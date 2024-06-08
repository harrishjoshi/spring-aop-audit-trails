package com.harrishjoshi.springaop.audit.trails.audit;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface AuditEventLogResponse {
    Integer getId();

    String getEntityName();

    String getFunctionCode();

    String getActionCode();

    String getField();

    String getOldValue();

    String getNewValue();

    String getEventStatus();

    String getErrorCode();

    String getErrorDetails();

    LocalDateTime getEventDate();

    String getCreatedOrModifiedBy();
}
