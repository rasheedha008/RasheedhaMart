# Week 2: Core Shopping Flow (Aug 3 – Aug 9)

## Goal
Deliver the MVP core user journey: Product Browsing $\rightarrow$ Cart Management $\rightarrow$ Mock Payment Checkout.

## Accomplishments
- **F2: Seller Product Management**: Built `Product.java`, `ProductDAO.java`, and `ProductService.java` supporting create, update, delete, stock tracking, and image URLs.
- **F3: Product Catalog & Search**: Implemented buyer catalog browsing and basic category filtering.
- **F4: Shopping Cart Flow**: Created `CartItem.java` and `CartDAO.java` supporting adding, quantity modification, item removal, and running subtotal calculation.
- **F5: Mock Payment Checkout**: Developed transactional order creation (`OrderDAOImpl.java`) with atomic stock deduction and cart clearing.
- **Demo Seed Data (`seed.sql`)**: Populated realistic users (Admin, Sellers, Buyers) and products for MVP evaluation.

## MVP Review Checkpoint Status
- Complete user journey validated: Register $\rightarrow$ Browse $\rightarrow$ Cart $\rightarrow$ Checkout.
- Seed data loaded and verified.
