package com.harrishjoshi.springaop.audit.trails.audit;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventLog {

    String entityName() default "";

    String functionCode() default "";

    ActionCode actionCode() default ActionCode.VIEW;

    boolean logRequest() default false;

    boolean logResponse() default false;
}
