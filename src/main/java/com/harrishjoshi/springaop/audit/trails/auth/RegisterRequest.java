package com.harrishjoshi.springaop.audit.trails.auth;

import com.harrishjoshi.springaop.audit.trails.user.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    private String lastName;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    String password;

    private Role role;
}
