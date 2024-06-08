package com.harrishjoshi.springaop.audit.trails.audit;

import com.harrishjoshi.springaop.audit.trails.user.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

public class AuditUtils {

    private AuditUtils() {
    }

    public static EventLogRequest getEventLogRequest() {
        return EventLogRequest.builder()
                .eventDate(LocalDateTime.now())
                .userId(getLoggedInUserId().orElse(null))
                .build();
    }

    public static Optional<Integer> getLoggedInUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        User user = (User) authentication.getPrincipal();

        return Optional.of(user.getId());
    }
}
