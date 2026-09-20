# RasheedhaMart — Premium Multi-Vendor E-Commerce Platform
**Java Servlets · JDBC · HikariCP · H2 Database · Embedded Server Container**

`RasheedhaMart` (`com.rasheedha.rasheedhamart`) is a high-performance, full-stack multi-vendor e-commerce application featuring buyer shopping flows, seller listing management, transactional checkout, customer reviews, system administration, and health monitoring.

---

## 🌐 Application Access & URLs

Once started, access the application via:
- **Web Portal URL**: [http://localhost:8080](http://localhost:8080)
- **Health Check API**: [http://localhost:8080/api/v1/health](http://localhost:8080/api/v1/health) (Returns `{"status":"UP","db":"UP"}`)

---

## ⚡ Quick Start Instructions

### Prerequisites
- **Java Development Kit (JDK 17+)**
- **Apache Maven 3.8+**

### Launching the Application
1. Open a terminal in the root directory:
   ```bash
   cd D:\RASHEEDHA_MART
   ```
2. Build and run tests with Maven:
   ```bash
   mvn clean test
   ```
3. Start the application server:
   ```bash
   mvn exec:java
   ```
   *Or on Windows, simply double-click:* `run.bat`

---

## 🔑 Demo Account Credentials

Default password for all seeded accounts: **`password123`**

| Role | Email | Password | Features |
|------|-------|----------|----------|
| **BUYER** | `buyer.john@gmail.com` | `password123` | Storefront browsing, instant add-to-cart, mock checkout, product reviews. |
| **SELLER** | `seller.rasheedha@rasheedhamart.com` | `password123` | Seller dashboard, inventory stock management, incoming order processing. |
| **ADMIN** | `admin@rasheedhamart.com` | `password123` | System user administration, global order audit, listing moderation. |

---

## 📁 Repository Directory Structure

```
D:\RASHEEDHA_MART\
├── src/             # Application Java Source Code & Static Web Assets
│   ├── main/java/com/rasheedha/rasheedhamart/
│   │   ├── controller/   # Servlets (Auth, Product, Cart, Checkout, Order, Review, Admin, Health)
│   │   ├── service/      # Business Logic Services
│   │   ├── dao/          # JDBC Data Access Layer (100% PreparedStatements)
│   │   ├── model/        # Entities (User, Product, CartItem, Order, Review)
│   │   ├── dto/          # Data Transfer Objects & Response Envelopes
│   │   ├── filter/       # AuthFilter & EncodingFilter
│   │   └── server/       # MainServer (Embedded Jetty Container)
│   └── main/resources/
│       ├── schema.sql    # Relational Database Schema
│       ├── seed.sql      # Seed Data Script
│       └── web/          # Blue-Themed Responsive Single Page UI (index.html)
├── week-1 to week-8/ # Weekly Progressive Milestones
├── pom.xml          # Maven Build Configuration
├── run.bat          # Windows Launcher Script
└── Dockerfile       # Container Deployment Descriptor
```

---

## 🛠️ Tech Stack & Architecture

- **Language**: Java 17+
- **Server**: Embedded Eclipse Jetty 9.4 (Servlets)
- **Database**: H2 Embedded Relational Database
- **Connection Pool**: HikariCP 5.1.0
- **Security**: jBCrypt Password Hashing, HTML Output Escaping (XSS Prevention), Session Regeneration
- **UI Framework**: HTML5, Modern Blue CSS Palette, Bootstrap 5, Bootstrap Icons
