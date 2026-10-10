# RasheedhaMart Sequence Diagrams

This document details the core runtime interactions across system components for **RasheedhaMart**.

## 1. Checkout & Order Generation Flow

```mermaid
sequenceDiagram
    autonumber
    actor Buyer as Buyer Browser
    participant Servlet as CheckoutServlet
    participant Auth as AuthFilter
    participant Service as OrderService
    participant CartDAO as CartDAOImpl
    participant ProdDAO as ProductDAOImpl
    participant OrderDAO as OrderDAOImpl
    participant DB as MySQL / HikariCP Pool

    Buyer->>Auth: POST /api/v1/checkout (Session Cookie)
    Auth-->>Servlet: Forward Validated Session (Role: BUYER)
    Servlet->>Service: processCheckout(buyerId, addressId)
    
    Service->>CartDAO: getCartItems(buyerId)
    CartDAO->>DB: SELECT * FROM cart_items WHERE buyer_id = ?
    DB-->>CartDAO: List<CartItem>
    CartDAO-->>Service: Return Cart Items

    loop For Each Item in Cart
        Service->>ProdDAO: getProductById(productId)
        ProdDAO->>DB: SELECT * FROM products WHERE id = ?
        DB-->>ProdDAO: Product (Check Stock & Price)
        ProdDAO-->>Service: Return Product
        Note over Service: Verify stockQuantity >= cartQuantity
    end

    Service->>DB: Begin Database Transaction
    Service->>OrderDAO: createOrder(order, items)
    OrderDAO->>DB: INSERT INTO orders & order_items
    
    loop Stock Reduction
        Service->>ProdDAO: updateStock(productId, newStock)
        ProdDAO->>DB: UPDATE products SET stock_quantity = ? WHERE id = ?
    end

    Service->>CartDAO: clearCart(buyerId)
    CartDAO->>DB: DELETE FROM cart_items WHERE buyer_id = ?
    
    Service->>DB: Commit Transaction
    Service-->>Servlet: Created Order DTO
    Servlet-->>Buyer: 201 Created (JSON Order Confirmation)
```

## 2. AI Chatbot Interaction Flow

```mermaid
sequenceDiagram
    autonumber
    actor User as Client Web UI
    participant Servlet as ChatServlet
    participant Limiter as RateLimitTracker
    participant Provider as ChatProvider (Mock / Gemini)

    User->>Servlet: POST /api/v1/chat { message: "What is delivery policy?" }
    Servlet->>Limiter: allowRequest(sessionId)
    
    alt Rate Limit Exceeded (> 10 msgs / min)
        Limiter-->>Servlet: false
        Servlet-->>User: 429 Too Many Requests
    else Rate Limit Passed
        Limiter-->>Servlet: true
        Note over Servlet: Validate message length <= 500 chars
        Servlet->>Provider: getResponse(message, userRole)
        Provider-->>Servlet: AI Generated / FAQ Response String
        Servlet-->>User: 200 OK { success: true, data: { reply: "..." } }
    end
```
