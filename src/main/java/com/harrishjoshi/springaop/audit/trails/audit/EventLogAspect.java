package com.harrishjoshi.springaop.audit.trails.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.harrishjoshi.springaop.audit.trails.helper.AppContext;
import com.harrishjoshi.springaop.audit.trails.helper.ContextKey;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Slf4j
@Component
class EventLogAspect {

    private final AuditEventLogService auditEventLogService;

    EventLogAspect(AuditEventLogService auditEventLogService) {
        this.auditEventLogService = auditEventLogService;
    }

    @Around("@annotation(EventLog)")
    public Object trace(ProceedingJoinPoint joinPoint) {
        var signature = (MethodSignature) joinPoint.getSignature();
        var eventLog = signature.getMethod().getAnnotation(EventLog.class);

        var eventLogRequest = AuditUtils.getEventLogRequest();
        eventLogRequest.setEntityName(eventLog.entityName());
        eventLogRequest.setFunctionCode(eventLog.functionCode());
        eventLogRequest.setActionCode(eventLog.actionCode());
        eventLogRequest.setEventDate(LocalDateTime.now());
        eventLogRequest.setUserId(AuditUtils.getLoggedInUserId().orElse(null));
        eventLogRequest.setField1("METHOD_SIGNATURE");
        eventLogRequest.setValue1(joinPoint.getSignature().toShortString());

        var request = (Object) null;
        if (eventLog.logRequest() && joinPoint.getArgs().length > 0) {
            request = joinPoint.getArgs()[0];
            log.debug("Request: {}", request);
        }

        var result = (Object) null;

        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            eventLogRequest.setException(e);
        } catch (Throwable e) {
            eventLogRequest.setErrorDetails(e.getLocalizedMessage());
        } finally {
            if (eventLog.logResponse()) {
                log.debug("Response: {}", result);
            }

            var entityId = AppContext.getEntityId();
            if (entityId != null) {
                eventLogRequest.setEntityId(Integer.valueOf(entityId));
            }

            if (ActionCode.UPDATE.equals(eventLog.actionCode())) {
                var preData = AppContext.get(ContextKey.PRE);
                var postData = AppContext.get(ContextKey.POST);
                setJsonDifference(eventLogRequest, preData, postData);
            }

            auditEventLogService.saveEventLog(eventLogRequest);
        }

        return result;
    }

    private void setJsonDifference(EventLogRequest eventLogRequest, Object preData, Object postData) {
        try {
            var jsonNode = JsonDifferenceUtils.getJsonDifference(preData, postData);
            eventLogRequest.setJsonDiff(jsonNode);
        } catch (JsonProcessingException e) {
            log.error("Error while calculating json difference", e);
        }
    }
}
