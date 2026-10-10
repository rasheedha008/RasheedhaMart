# Contributing to RasheedhaMart

Thank you for your interest in contributing to **RasheedhaMart**!

## Development Guidelines

1. **Architecture & Layering**:
   - Maintain clear separation between Servlets (Controllers), Services (Business Logic), DAOs (Data Access), and Entities (Models).
   - Servlets must NOT execute raw SQL statements; delegate data access to DAOs.
   - Use DTOs for client-facing API responses.

2. **Database & Persistence**:
   - All SQL queries in DAOs must use `PreparedStatement` with parameterized inputs to prevent SQL Injection.
   - Version all database schema changes in `database/migrations/` using Flyway conventions (`V00X__description.sql`) and keep `schema.sql` and `seed.sql` updated.

3. **Security Standards**:
   - Store passwords exclusively using `jBCrypt` hashes.
   - Perform server-side validation and authorization checks for all endpoints. Never rely solely on front-end UI element visibility.
   - Escapes HTML outputs when serving string data to mitigate XSS vulnerabilities (`XSSUtil`).

4. **Directory & Module Naming**:
   - Never use academic step numbers (`week-1`, `week-2`, etc.) in directory names or code packages.
   - Always group features by functional domain (e.g. `AddressService`, `MarketingService`, `AnalyticsService`).

5. **Branching & Pull Requests**:
   - Create feature branches named `feature/<domain>-<description>` or `fix/<description>`.
   - Ensure `mvn clean verify` passes without errors before submitting pull requests.
