# Project Retrospective: RasheedhaMart

## 🎯 Project Overview & Objective

The goal of the **RasheedhaMart** project was to build a full-stack, multi-vendor e-commerce web platform for a 2nd-year Computer Science and Engineering student portfolio. The project emphasizes production-grade Java design patterns, robust security practices, multi-role access control (Buyer, Seller, Admin), delivery address management, progressive order tracking, verified customer reviews, admin marketing & financial analytics, AI chatbot support, and deployment readiness (Tomcat & Node/Render).

---

## 🌟 Key Accomplishments

1. **Domain-Driven Modular Architecture**:
   - Re-organized legacy structures into professional functional domain packages (`chatbot`, `controller`, `dao`, `dto`, `exception`, `filter`, `listener`, `model`, `server`, `service`, `util`).
   - Removed all `week-*` references to maintain professional repository hygiene.

2. **Multi-Role Security & Access Control**:
   - Implemented three distinct roles: `BUYER`, `SELLER`, and `ADMIN`.
   - Admin account is seeded strictly via `seed.sql` with public registration disabled.
   - Enforced jBCrypt password hashing and server-side authorization checks on all protected API routes.

3. **Complete End-to-End E-Commerce Flow**:
   - Storefront product browsing, keyword + category search, and stock control.
   - Saved shipping addresses management with default address selection.
   - Cart management with server-calculated subtotals and mock payment checkout.
   - Order delivery status tracking (`PENDING` -> `CONFIRMED` -> `SHIPPED` -> `DELIVERED`).
   - Verified buyer product reviews (restricted to users with completed orders for the product).

4. **Admin Marketing & Financial Analytics**:
   - Admin promotional marketing banner management.
   - Real-time financial analytics dashboard displaying total revenue, estimated costs (70%), gross profit (30%), profit margins, active user counts, and category breakdown.

5. **AI Chatbot Integration**:
   - Built dual-provider architecture (`MockChatProvider` and `GeminiChatProvider`).
   - Included rate limiting (10 msgs/min per session) and a 500-character input cap.

6. **DevOps, Packaging & Deployment Synchronization**:
   - Added database migration scripts (`V001` through `V008`).
   - HikariCP connection pool lifecycle listener (`DatabaseConnectionListener`).
   - `server.js` Express adapter updated to full parity with the Java API endpoints.
   - Automated GitHub Actions CI workflow (`build.yml`), Dockerfile, and Render blueprint (`render.yaml`).

---

## 📈 Technical Metrics & Quality Assurance

- **Build Tool**: Apache Maven
- **Compilation**: Clean compilation under JDK 17
- **Test Coverage**: JUnit 5 & Mockito test suites covering DAOs against H2 database and core Service logic.
- **SQL Security**: 100% PreparedStatements across all DAO implementations.

---

## 🔮 Future Enhancements

- Integration with live payment gateways (Razorpay / Stripe) replacing mock payment.
- Real-time WebSockets / Push Notifications for order delivery state transitions.
- Advanced AI product recommendations powered by vector search.
