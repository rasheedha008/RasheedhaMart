# RasheedhaMart Entity-Relationship (ER) Diagram

This document describes the complete relational database architecture for **RasheedhaMart**, a full-stack multi-vendor e-commerce platform.

## Mermaid ER Diagram

```mermaid
erDiagram
    USERS ||--o{ PRODUCTS : "sells"
    USERS ||--o{ ADDRESSES : "manages"
    USERS ||--o{ ORDERS : "places"
    USERS ||--o{ REVIEWS : "writes"
    USERS ||--o{ CART_ITEMS : "holds"
    
    PRODUCTS ||--o{ CART_ITEMS : "included_in"
    PRODUCTS ||--o{ ORDER_ITEMS : "ordered_in"
    PRODUCTS ||--o{ REVIEWS : "receives"
    
    ADDRESSES ||--o{ ORDERS : "delivers_to"
    ORDERS ||--o{ ORDER_ITEMS : "contains"

    USERS {
        bigint id PK
        varchar email UK
        varchar password_hash
        varchar full_name
        varchar role "BUYER | SELLER | ADMIN"
        timestamp created_at
    }

    PRODUCTS {
        bigint id PK
        bigint seller_id FK
        varchar name
        text description
        decimal price
        int stock_quantity
        varchar category
        varchar image_url
        timestamp created_at
    }

    ADDRESSES {
        bigint id PK
        bigint user_id FK
        varchar full_name
        varchar phone
        varchar address_line1
        varchar address_line2
        varchar city
        varchar state
        varchar postal_code
        varchar country
        boolean is_default
    }

    CART_ITEMS {
        bigint id PK
        bigint buyer_id FK
        bigint product_id FK
        int quantity
        timestamp created_at
    }

    ORDERS {
        bigint id PK
        bigint buyer_id FK
        bigint address_id FK
        decimal total_amount
        varchar status "PENDING | CONFIRMED | SHIPPED | DELIVERED | CANCELLED"
        timestamp created_at
    }

    ORDER_ITEMS {
        bigint id PK
        bigint order_id FK
        bigint product_id FK
        bigint seller_id FK
        int quantity
        decimal unit_price
    }

    REVIEWS {
        bigint id PK
        bigint product_id FK
        bigint buyer_id FK
        int rating "1-5 Stars"
        text comment
        timestamp created_at
    }

    PROMOTIONS {
        bigint id PK
        varchar title
        varchar banner_url
        boolean is_active
        timestamp created_at
    }
```

## Schema Entities & Relationships

1. **USERS**: Represents system actors with roles (`BUYER`, `SELLER`, `ADMIN`). Admin accounts are seeded in database and cannot be registered publicly.
2. **PRODUCTS**: Multi-vendor product catalog listings tied to `SELLER` user IDs with price, stock, and category attributes.
3. **ADDRESSES**: Saved shipping destinations per `BUYER` user, supporting default flags and detailed postal fields.
4. **CART_ITEMS**: Active shopping cart items per `BUYER` with automatic duplicate aggregation and quantity tracking.
5. **ORDERS & ORDER_ITEMS**: Atomic transaction records linking buyer, selected shipping address, snapshot order items, seller IDs, subtotal, and progressive lifecycle status.
6. **REVIEWS**: Verified customer ratings (1 to 5 stars) and feedback restricted to buyers who purchased and received the target item.
7. **PROMOTIONS**: Platform marketing banners and active campaign announcements managed exclusively by `ADMIN`.
