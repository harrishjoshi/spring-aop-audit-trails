package com.harrishjoshi.springaop.audit.trails.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<Integer> {

    @Override
    @NonNull
    public Optional<Integer> getCurrentAuditor() {
        return AuditUtils.getLoggedInUserId();
    }
}