package com.harrishjoshi.springaop.audit.trails.audit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_audit_event_log")
public class AuditEventLog {

    @Id
    @GeneratedValue
    private Long id;
    private String entityName;
    private String functionCode;
    @Enumerated(EnumType.STRING)
    private ActionCode actionCode;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EventStatus eventStatus = EventStatus.SUCCESS;
    @ColumnTransformer(write = "?::jsonb")
    @Column(columnDefinition = "jsonb")
    private String jsonDiff;
    private String field1;
    private String value1;
    private String field2;
    private String value2;
    private String errorCode;
    private String errorDetails;
    private LocalDateTime eventDate;
    private Integer userId;
}
