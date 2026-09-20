# Week 4: Admin Panel & Order History (Aug 17 – Aug 23)

## Goal
Finish the Seller Dashboard order views and implement the System Admin Moderation Panel (F6 & F7).

## Accomplishments
- **F6 (Seller Half)**: Enabled sellers to view incoming customer orders for items in their catalog (`/api/v1/orders/seller`).
- **F6 (Buyer Half)**: Delivered detailed buyer order history views with line items, prices, and status updates.
- **F7 (Admin Panel)**: Created `AdminService.java` and `AdminServlet.java` permitting admins to inspect all users, view system-wide order history, moderate/delete listings, and deactivate accounts.
- **Security Enforcement**: Applied `AuthFilter` session checks and role-based permissions (`ADMIN`, `SELLER`, `BUYER`) across all newly added routes.
