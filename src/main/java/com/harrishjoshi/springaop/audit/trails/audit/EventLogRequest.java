package com.harrishjoshi.springaop.audit.trails.audit;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EventLogRequest {

    private String entityName;
    private String functionCode;
    private ActionCode actionCode;
    @Builder.Default
    private EventStatus eventStatus = EventStatus.SUCCESS;
    private Object jsonDiff;
    private String field1;
    private String value1;
    private String field2;
    private String value2;
    private String errorCode;
    private String errorDetails;
    private LocalDateTime eventDate;
    private Integer userId;

    public void setException(Exception e) {
        this.errorCode = e.getClass().getSimpleName();
        this.errorDetails = e.getMessage();
        this.eventStatus = EventStatus.FAILED;
    }
}
