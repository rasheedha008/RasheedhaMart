package com.rasheedha.rasheedhamart;

import com.rasheedha.rasheedhamart.dao.OrderDAO;
import com.rasheedha.rasheedhamart.dao.ProductDAO;
import com.rasheedha.rasheedhamart.dao.UserDAO;
import com.rasheedha.rasheedhamart.model.Order;
import com.rasheedha.rasheedhamart.model.Product;
import com.rasheedha.rasheedhamart.model.User;
import com.rasheedha.rasheedhamart.service.AnalyticsService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminServiceTest {

    @Test
    public void testUserSuspensionModelFlag() {
        User user = new User(1L, "test@buyer.com", "hash", "Test Buyer", "BUYER", true, new Timestamp(System.currentTimeMillis()));
        assertTrue(user.isActive());

        user.setActive(false);
        assertFalse(user.isActive());
    }

    @Test
    public void testProductModerationHideFlag() {
        Product product = new Product(1L, 2L, "Inappropriate Item", "Desc", BigDecimal.valueOf(100), 5, "General", "url", true, false, null);
        assertFalse(product.isHidden());

        product.setHidden(true);
        assertTrue(product.isHidden());
    }

    @Test
    public void testAnalyticsServiceFinancialCalculations() {
        UserDAO userDAO = mock(UserDAO.class);
        ProductDAO productDAO = mock(ProductDAO.class);
        OrderDAO orderDAO = mock(OrderDAO.class);

        User buyer = new User(1L, "buyer@test.com", "hash", "Buyer", "BUYER", true, null);
        User seller = new User(2L, "seller@test.com", "hash", "Seller", "SELLER", true, null);
        when(userDAO.findAll()).thenReturn(Arrays.asList(buyer, seller));

        Order order1 = new Order(1L, 1L, BigDecimal.valueOf(1000), "DELIVERED", new Timestamp(System.currentTimeMillis()));
        Order order2 = new Order(2L, 1L, BigDecimal.valueOf(500), "SHIPPED", new Timestamp(System.currentTimeMillis()));
        when(orderDAO.findAll()).thenReturn(Arrays.asList(order1, order2));

        AnalyticsService analyticsService = new AnalyticsService(userDAO, productDAO, orderDAO);
        Map<String, Object> summary = analyticsService.getAdminAnalyticsSummary();

        assertNotNull(summary);
        assertEquals(BigDecimal.valueOf(1500), summary.get("totalRevenue"));
        assertEquals(BigDecimal.valueOf(1050.0), summary.get("estimatedCost"));
        assertEquals(BigDecimal.valueOf(450.0), summary.get("estimatedProfit"));
        assertEquals(2, summary.get("totalOrders"));
    }
}
