# Spring AOP Audit Trails

Spring Boot application to demonstrates the design for tracking changes to database records at the column level using PostgreSQL's JSONB data type. It uses Spring AOP with the `@Around` annotation to intercept database operations and log changes. Additionally, the application ensures secure access with JWT-based authentication and authorization, filtering each request for security. It also leverages JPA auditing features to automatically capture and store metadata such as creation and modification timestamps for each entity.

**Key Features:**
- **Column-Level Change Tracking**: Uses Spring AOP to log changes in JSONB format in PostgreSQL.
- **JWT Security**: Secures each request with JWT-based authentication and authorization.
- **JPA Auditing**: Automatically records metadata (created by, created date, last modified by, last modified date) for all entities.
- **Event Log Reports**: Each column changes reports according to entity ID and auditor ID.