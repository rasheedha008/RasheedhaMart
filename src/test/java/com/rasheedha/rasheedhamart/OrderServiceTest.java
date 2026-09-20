package com.rasheedha.rasheedhamart;

import com.rasheedha.rasheedhamart.dao.CartDAO;
import com.rasheedha.rasheedhamart.dao.OrderDAO;
import com.rasheedha.rasheedhamart.exception.ValidationException;
import com.rasheedha.rasheedhamart.model.Order;

import com.rasheedha.rasheedhamart.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    private OrderDAO orderDAO;
    private CartDAO cartDAO;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderDAO = mock(OrderDAO.class);
        cartDAO = mock(CartDAO.class);
        orderService = new OrderService(orderDAO, cartDAO);
    }

    @Test
    void testValidStatusTransition() {
        Order pendingOrder = new Order(10L, 1L, null, "PENDING", null);
        when(orderDAO.findById(10L)).thenReturn(Optional.of(pendingOrder));
        when(orderDAO.updateStatus(10L, "CONFIRMED")).thenReturn(true);

        Order updated = orderService.updateOrderStatus(10L, "CONFIRMED");
        assertEquals("CONFIRMED", updated.getStatus());
        verify(orderDAO).updateStatus(10L, "CONFIRMED");
    }

    @Test
    void testInvalidStatusTransitionThrowsException() {
        Order pendingOrder = new Order(10L, 1L, null, "PENDING", null);
        when(orderDAO.findById(10L)).thenReturn(Optional.of(pendingOrder));

        // PENDING directly to DELIVERED is illegal
        assertThrows(ValidationException.class, () -> orderService.updateOrderStatus(10L, "DELIVERED"));
    }
}
