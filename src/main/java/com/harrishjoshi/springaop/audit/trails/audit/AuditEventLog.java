package com.harrishjoshi.springaop.audit.trails.audit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;

import java.time.LocalDateTime;

import static com.harrishjoshi.springaop.audit.trails.audit.AuditEventLogQueries.FIND_ALL_QUERY;
import static com.harrishjoshi.springaop.audit.trails.audit.AuditEventLogQueries.FIND_BY_USER_ID_OR_ENTITY_ID_QUERY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedNativeQueries(value = {
        @NamedNativeQuery(
                name = "AuditEventLog.findAllBy",
                query = FIND_ALL_QUERY,
                resultSetMapping = "Mapping.AuditEventLogResponse"
        ),
        @NamedNativeQuery(
                name = "AuditEventLog.getAuditEventLogByUserIdOrEntityId",
                query = FIND_BY_USER_ID_OR_ENTITY_ID_QUERY,
                resultSetMapping = "Mapping.AuditEventLogResponse"
        )
})
@SqlResultSetMapping(name = "Mapping.AuditEventLogResponse",
        classes = @ConstructorResult(
                targetClass = AuditEventLogResponse.class,
                columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "entityName", type = String.class),
                        @ColumnResult(name = "functionCode", type = String.class),
                        @ColumnResult(name = "actionCode", type = ActionCode.class),
                        @ColumnResult(name = "field", type = String.class),
                        @ColumnResult(name = "old_value", type = String.class),
                        @ColumnResult(name = "new_value", type = String.class),
                        @ColumnResult(name = "eventStatus", type = EventStatus.class),
                        @ColumnResult(name = "errorCode", type = String.class),
                        @ColumnResult(name = "errorDetails", type = String.class),
                        @ColumnResult(name = "eventDate", type = LocalDateTime.class),
                        @ColumnResult(name = "createdOrModifiedBy", type = String.class)
                }
        )
)
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
    private Integer entityId;
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
