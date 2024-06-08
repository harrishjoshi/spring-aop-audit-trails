package com.harrishjoshi.springaop.audit.trails.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.util.HashMap;
import java.util.Map;

public class AppContext {

    static ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();

    private static final ThreadLocal<Map<String, Object>> contextMap = new ThreadLocal<>();

    public static void set(ContextKey key, Object value) {
        getContextMap().put(key.name(), value);
    }

    public static Object get(ContextKey key) {
        return getContextMap().get(key.name());
    }

    public static void set(String key, Object value) {
        getContextMap().put(key, value);
    }

    public static void clear() {
        contextMap.remove();
    }

    private static Map<String, Object> getContextMap() {
        if (contextMap.get() == null)
            contextMap.set(new HashMap<>());

        return contextMap.get();
    }

    public static String getEmailId() {
        return (String) get(ContextKey.EMAIL);
    }

    public static Integer getUserId() {
        return (Integer) get(ContextKey.USER_ID);
    }

    public static void setDetails(ContextKey key, Object object) {
        if (key.equals(ContextKey.PRE) || key.equals(ContextKey.POST)) {
            try {
                AppContext.set(key.name(), objectMapper.writeValueAsString(object));
            } catch (Exception ex) {
                AppContext.set(key.name(), ex.getMessage());
            }
        }
    }
}
