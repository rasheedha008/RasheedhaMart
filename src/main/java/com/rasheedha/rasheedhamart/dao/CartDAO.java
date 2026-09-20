package com.rasheedha.rasheedhamart.dao;

import com.rasheedha.rasheedhamart.model.CartItem;
import java.util.List;

public interface CartDAO {
    CartItem addItem(Long buyerId, Long productId, int quantity);
    CartItem updateQuantity(Long buyerId, Long productId, int quantity);
    boolean removeItem(Long buyerId, Long productId);
    boolean clearCart(Long buyerId);
    List<CartItem> getCartItems(Long buyerId);
}
