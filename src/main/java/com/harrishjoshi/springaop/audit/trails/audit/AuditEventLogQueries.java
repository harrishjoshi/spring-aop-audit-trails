package com.harrishjoshi.springaop.audit.trails.audit;

public class AuditEventLogQueries {

    private static final String BASE_QUERY = "SELECT " +
            "e.id, " +
            "e.entity_name AS entityName, " +
            "e.function_code AS functionCode, " +
            "e.action_code AS actionCode, " +
            "data.field AS field, " +
            "data.value ->> 'old' AS old_value, " +
            "data.value ->> 'new' AS new_value, " +
            "e.event_status AS eventStatus, " +
            "e.error_code AS errorCode, " +
            "e.error_details AS errorDetails, " +
            "e.event_date AS eventDate, " +
            "u.first_name AS createdOrModifiedBy " +
            "FROM tbl_audit_event_log e " +
            "LEFT JOIN tbl_user u ON u.id = e.user_id " +
            "LEFT JOIN LATERAL jsonb_each(e.json_diff) AS data(field, value) " +
            "ON e.json_diff IS NOT NULL";

    private static final String ORDER_BY_EVENT_DATE_DESC = " ORDER BY e.event_date DESC";

    private static final String USER_ID_OR_ENTITY_ID_WHERE_CLAUSE = " WHERE (e.user_id = ?1 OR e.entity_id = ?1)";

    public static final String FIND_ALL_QUERY = BASE_QUERY + ORDER_BY_EVENT_DATE_DESC;

    public static final String FIND_BY_USER_ID_OR_ENTITY_ID_QUERY = BASE_QUERY + USER_ID_OR_ENTITY_ID_WHERE_CLAUSE + ORDER_BY_EVENT_DATE_DESC;
}
