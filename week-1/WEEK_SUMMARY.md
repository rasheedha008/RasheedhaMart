# Week 1: Authentication & Base DAO Layer (Jul 27 – Aug 2)

## Goal
Lock the DB Schema v1 and establish user authentication, connection pooling with HikariCP, and password security using BCrypt.

## Accomplishments
- **Database Schema v1 (`schema.sql`)**: Created foundational tables (`users`, `products`, `orders`, `order_items`, `cart_items`, `reviews`) with proper data types (`DECIMAL(10,2)` for currency, `UNIQUE` email, foreign key constraints).
- **BCrypt Password Hashing (`PasswordUtil.java`)**: Secured password storage using `jBCrypt` (never plaintext or MD5/SHA1).
- **HikariCP Connection Pool (`DatabaseManager.java`)**: Configured high-performance connection pool with H2 in-memory/embedded database.
- **Base DAO Implementation (`UserDAO.java`, `UserDAOImpl.java`)**: Standardized data access layer using `PreparedStatement` and try-with-resources.
- **Session Security & Filters (`AuthFilter.java`, `EncodingFilter.java`)**: Enforced UTF-8 encoding and HTTP session checks with explicit 30-minute session timeout.
- **Unit Testing**: Verified User CRUD operations against embedded H2 database with JUnit 5.

## Standard Rules Compliance
- 100% PreparedStatements (Zero SQL String Concatenation)
- Password BCrypt Hashing
- Clean Layering: Controller -> Service -> DAO -> Model
