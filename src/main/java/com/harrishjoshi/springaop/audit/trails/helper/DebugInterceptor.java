package com.harrishjoshi.springaop.audit.trails.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ClassUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.Collection;

@Aspect
@Slf4j
@Component
public class DebugInterceptor {

    private final static int displayListSize = 5;

    @Around("execution(* *..*Controller.*(..))")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object output = null;
        String exception = null;
        StringBuilder logBuilder;
        var displayName = ClassUtils.getShortClassName(joinPoint.getTarget().getClass()) + "." + joinPoint.getSignature().getName();
        var isLogEnabled = log.isDebugEnabled();
        var time = System.currentTimeMillis();

        if (isLogEnabled) {
            logBuilder = new StringBuilder(displayName);
            logBuilder.append("-IN: ");

            Object[] params = joinPoint.getArgs();
            for (int i = 0; i < params.length; i++) {
                logObject(logBuilder, i, params[i]);
                if (i < params.length - 1) logBuilder.append(", ");
            }

            log.debug(logBuilder.toString());
        }

        try {
            output = joinPoint.proceed();
        } catch (Throwable e) {
            exception = ClassUtils.getShortClassName(e.getClass());
            throw e;
        } finally {
            if (isLogEnabled) {
                logBuilder = new StringBuilder(displayName);
                logBuilder.append("-OUT: ");
                logObject(logBuilder, 0, exception == null ? output : exception);

                double total = (System.currentTimeMillis() - time);

                logBuilder.append(" (").append(total / 1000).append("s)");
                log.debug(logBuilder.toString());
            }
        }

        return output;
    }

    private void logObject(StringBuilder logBuilder, int index, Object param) {
        try {
            logBuilder.append(index).append("=[");

            if (param == null)
                logBuilder.append("null");
            else if (param instanceof Collection) {
                int size = CollectionUtils.size(param);
                if (size > displayListSize)
                    logBuilder.append("List-").append(size);
                else
                    logBuilder.append(marshal(param));
            } else
                logBuilder.append(param);

            logBuilder.append("]");
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    public String marshal(Object object) throws Exception {
        var writer = new StringWriter();
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.writeValue(writer, object);

        return writer.toString();
    }
}
