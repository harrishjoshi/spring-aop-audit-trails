package com.harrishjoshi.springaop.audit.trails.audit;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<Integer> {

    @Override
    public Optional<Integer> getCurrentAuditor() {
        return AuditUtils.getLoggedInUserId();
    }
}