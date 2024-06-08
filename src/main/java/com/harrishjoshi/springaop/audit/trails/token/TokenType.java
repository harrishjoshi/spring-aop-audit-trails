package com.harrishjoshi.springaop.audit.trails.token;

public enum TokenType {
    BEARER("Bearer ");

    private final String value;

    TokenType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
