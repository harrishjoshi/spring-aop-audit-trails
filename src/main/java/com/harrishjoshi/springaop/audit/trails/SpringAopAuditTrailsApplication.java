package com.harrishjoshi.springaop.audit.trails;

import com.harrishjoshi.springaop.audit.trails.auth.AuthenticationService;
import com.harrishjoshi.springaop.audit.trails.auth.RegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static com.harrishjoshi.springaop.audit.trails.user.Role.ADMIN;
import static com.harrishjoshi.springaop.audit.trails.user.Role.MANAGER;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class SpringAopAuditTrailsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAopAuditTrailsApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(
            AuthenticationService service
    ) {
        return args -> {
            var admin = RegisterRequest.builder()
                    .firstName("Admin")
                    .lastName("Admin")
                    .email("admin@yopmail.com")
                    .password("password")
                    .role(ADMIN)
                    .build();
            System.out.println("Admin token: " + service.register(admin).accessToken());

            var manager = RegisterRequest.builder()
                    .firstName("Manager")
                    .lastName("Manager")
                    .email("manager@yopmail.com")
                    .password("password")
                    .role(MANAGER)
                    .build();
            System.out.println("Manager token: " + service.register(manager).accessToken());
        };
    }
}
