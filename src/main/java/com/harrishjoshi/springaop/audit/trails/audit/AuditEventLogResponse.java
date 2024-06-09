package com.harrishjoshi.springaop.audit.trails.audit;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuditEventLogResponse(
        Integer id,
        String entityName,
        String functionCode,
        ActionCode actionCode,
        String field,
        String oldValue,
        String newValue,
        EventStatus eventStatus,
        String errorCode,
        String errorDetails,
        LocalDateTime eventDate,
        String createdOrModifiedBy
) {
}
