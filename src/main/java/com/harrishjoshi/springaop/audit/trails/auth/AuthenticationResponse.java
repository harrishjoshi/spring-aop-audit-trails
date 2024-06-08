package com.harrishjoshi.springaop.audit.trails.auth;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String accessToken,
        String refreshToken
) {
}
