package com.harrishjoshi.springaop.audit.trails.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuditEventLogRepository extends JpaRepository<AuditEventLog, Integer> {


    @Query(value = """
                SELECT 
                    e.id, 
                    e.entity_name AS entityName, 
                    e.function_code AS functionCode, 
                    e.action_code AS actionCode, 
                    data.field AS field, 
                    data.value ->> 'old' AS old_value, 
                    data.value ->> 'new' AS new_value, 
                    e.event_status AS eventStatus,
                    e.error_code AS errorCode,
                    e.error_details AS errorDetails,
                    e.event_date AS eventDate, 
                    u.first_name AS createdOrModifiedBy 
                FROM 
                    tbl_audit_event_log e 
                LEFT JOIN tbl_user u ON u.id = e.user_id 
                LEFT JOIN LATERAL jsonb_each(e.json_diff) AS data(field, value) ON e.json_diff IS NOT NULL 
                ORDER BY e.event_date DESC
            """, nativeQuery = true)
    List<AuditEventLogResponse> findAllBy();
}
