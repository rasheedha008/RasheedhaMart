# RasheedhaMart — Full-Stack Multi-Vendor E-Commerce Platform

[![Java CI](https://github.com/rasheedhamart/rasheedhamart/actions/workflows/build.yml/badge.svg)](https://github.com/rasheedhamart/rasheedhamart/actions/workflows/build.yml)
[![Java Version](https://img.shields.io/badge/Java-17%2B-blue.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

**RasheedhaMart** is a production-style, multi-vendor e-commerce web platform engineered using Java 17, Servlets, JDBC, HikariCP, MySQL / H2, HTML5/CSS3/JavaScript, and Node.js adapter support. Designed with clean layered architecture, security controls, verified buyer reviews, address management, order delivery status tracking, marketing management, profit & loss financial analytics, and an interactive AI chatbot.

---

## 🎨 Theme & Visual Palette

The application uses an aesthetic, professional design language:
- **Primary Headers & Top Nav**: Deep Navy Blue (`#0a192f` / `#0f2744`)
- **Action Buttons & Accents**: Electric Sky Blue (`#0284c7` / `#38bdf8`)
- **Background & Card Surfaces**: Neutral slate `#f8fafc` with crisp white cards (`#ffffff`) and soft shadows

---

## 🌐 Quick Access URLs

When running locally (via Java Server or Node Server):
- **Web Portal URL**: [http://localhost:8080](http://localhost:8080)
- **Health Check API**: [http://localhost:8080/api/v1/health](http://localhost:8080/api/v1/health) (Returns `{"status":"UP","db":"UP"}`)

---

## 🔑 Pre-Seeded User Credentials

All seeded accounts use password: **`password123`**

| Role | Email | Password | Primary Capabilities |
|------|-------|----------|----------------------|
| **BUYER** | `buyer.john@gmail.com` | `password123` | Product search/filter, cart management, saved shipping addresses, checkout, order tracking, verified review submission, AI chatbot. |
| **SELLER** | `seller.rasheedha@rasheedhamart.com` | `password123` | Seller dashboard, inventory stock management, add/edit/delete product listings, view incoming orders. |
| **ADMIN** | `admin@rasheedhamart.com` | `password123` | System user moderation, global order audit, promotional banner management, Profit & Loss financial analytics. |

*Note: There is intentionally NO public Admin registration page. System administrators exist only via database seeds.*

---

## 🏗️ Domain Modules Overview

The platform is structured into clear functional domain modules:

1. **Authentication & User Management**: Session control, jBCrypt password hashing, role-based access checks.
2. **Product Catalog**: Multi-vendor product management, search, category filters, pagination, stock control.
3. **Shopping Cart**: Server-side subtotal calculation, stock validation, item quantity controls.
4. **Address Management**: Delivery address CRUD, default shipping address selection.
5. **Checkout & Mock Payment**: Cart validation, atomic transactional order placement, stock deduction.
6. **Order Management & Delivery Tracking**: Progressive lifecycle tracking (`PENDING` -> `CONFIRMED` -> `SHIPPED` -> `DELIVERED`).
7. **Reviews & Ratings**: Verified customer star ratings (1–5) and text feedback.
8. **Seller Dashboard**: Product performance metrics, inventory alerts, seller order fulfillment.
9. **Admin Marketing**: Promotional campaign banners management.
10. **Admin Financial Analytics**: Revenue, cost estimation, profit margin calculations, and category sales charts.
11. **AI Chatbot**: 24/7 assistant with rate limiting (10 msgs/min), 500-char cap, and mock/Gemini provider integration.
12. **Database & Persistence**: HikariCP connection pool, Flyway/versioned SQL migrations, PreparedStatements.

---

## 📁 Repository Structure

```
D:\RasheedhaMart\
├── .github/workflows/
│   └── build.yml               # GitHub Actions CI/CD Pipeline
├── database/
│   ├── migrations/             # V001__create_users.sql to V008__create_promotions.sql
│   ├── schema.sql              # Master Relational DDL Schema
│   └── seed.sql                # Production Seed Data
├── docs/
│   └── architecture/           # Architecture Specifications
│       ├── ER_DIAGRAM.md       # Entity-Relationship Diagrams
│       ├── USE_CASE_DIAGRAM.md # Actor & System Boundary Flows
│       └── SEQUENCE_DIAGRAM.md # Runtime Execution Flows
├── src/
│   ├── main/java/com/rasheedha/rasheedhamart/
│   │   ├── chatbot/            # AI Chatbot Providers & Rate Limiter
│   │   ├── controller/         # Servlet Controllers
│   │   ├── dao/                # JDBC Data Access Layer (100% PreparedStatements)
│   │   ├── dto/                # Data Transfer Objects
│   │   ├── exception/          # Domain Exceptions
│   │   ├── filter/             # AuthFilter & EncodingFilter
│   │   ├── listener/           # DatabaseConnectionListener (HikariCP lifecycle)
│   │   ├── model/              # Domain Entities
│   │   ├── server/             # MainServer Embedded Container
│   │   ├── service/            # Core Business Services
│   │   └── util/               # Security & Validation Utilities
│   └── main/resources/
│       ├── config.properties.example # Property settings template
│       ├── logback.xml         # Structured Logging XML
│       ├── schema.sql          # Classpath fallback schema
│       ├── seed.sql            # Classpath fallback seed
│       └── web/index.html      # Responsive Navy/Sky Blue UI
├── .env.example                # Deployment Environment Template
├── Dockerfile                  # Container Deployment Specification
├── package.json                # Node.js Adapter Dependencies
├── pom.xml                     # Maven Build & Dependencies
├── render.yaml                 # Render Deployment Blueprint
├── run.bat                     # Windows Startup Script
└── server.js                   # Node.js Express Runtime Sync Adapter
```

---

## 🚀 Installation & Running

### Option 1: Java Maven Server (Standard Production Backend)
```bash
# 1. Clean build and run unit tests
mvn clean verify

# 2. Start Embedded Server Container
mvn exec:java
```

### Option 2: Node.js Express Server (Cloud Deployment / Render)
```bash
# 1. Install Node dependencies
npm install

# 2. Start Express Server
npm start
```

---

## 🧪 Testing & Verification

Run the test suite with Maven:
```bash
mvn test
```
Includes DAO tests against H2 in-memory database and Mockito unit tests for service layer validation.

---

## 📜 Documentation & Specifications

- [Entity-Relationship Diagram](docs/architecture/ER_DIAGRAM.md)
- [Use Case Diagram](docs/architecture/USE_CASE_DIAGRAM.md)
- [Sequence Diagram](docs/architecture/SEQUENCE_DIAGRAM.md)
- [Contribution Guidelines](CONTRIBUTING.md)
- [Project Retrospective](RETRO.md)

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
