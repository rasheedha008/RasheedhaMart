package com.rasheedha.rasheedhamart.service;

import com.rasheedha.rasheedhamart.dao.OrderDAO;
import com.rasheedha.rasheedhamart.dao.ProductDAO;
import com.rasheedha.rasheedhamart.dao.UserDAO;
import com.rasheedha.rasheedhamart.model.Order;
import com.rasheedha.rasheedhamart.model.OrderItem;
import com.rasheedha.rasheedhamart.model.Product;
import com.rasheedha.rasheedhamart.model.User;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyticsService {

    private final UserDAO userDAO;
    private final ProductDAO productDAO;
    private final OrderDAO orderDAO;

    public AnalyticsService(UserDAO userDAO, ProductDAO productDAO, OrderDAO orderDAO) {
        this.userDAO = userDAO;
        this.productDAO = productDAO;
        this.orderDAO = orderDAO;
    }

    public Map<String, Object> getAdminAnalyticsSummary() {
        List<User> users = userDAO.findAll();
        List<Order> orders = orderDAO.findAll();

        long buyerCount = users.stream().filter(u -> "BUYER".equalsIgnoreCase(u.getRole())).count();
        long sellerCount = users.stream().filter(u -> "SELLER".equalsIgnoreCase(u.getRole())).count();

        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal estimatedCost = BigDecimal.ZERO;
        Map<String, BigDecimal> salesByCategory = new HashMap<>();

        for (Order order : orders) {
            totalRevenue = totalRevenue.add(order.getTotalAmount());
            if (order.getItems() != null) {
                for (OrderItem item : order.getItems()) {
                    BigDecimal itemCost = item.getUnitPrice().multiply(BigDecimal.valueOf(0.70)).multiply(BigDecimal.valueOf(item.getQuantity()));
                    estimatedCost = estimatedCost.add(itemCost);

                    if (item.getProduct() != null && item.getProduct().getCategory() != null) {
                        String cat = item.getProduct().getCategory();
                        BigDecimal catSub = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                        salesByCategory.put(cat, salesByCategory.getOrDefault(cat, BigDecimal.ZERO).add(catSub));
                    }
                }
            }
        }

        if (estimatedCost.compareTo(BigDecimal.ZERO) == 0 && totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
            estimatedCost = totalRevenue.multiply(BigDecimal.valueOf(0.70));
        }

        BigDecimal estimatedProfit = totalRevenue.subtract(estimatedCost);
        BigDecimal estimatedLoss = estimatedProfit.compareTo(BigDecimal.ZERO) < 0 ? estimatedProfit.abs() : BigDecimal.ZERO;
        if (estimatedProfit.compareTo(BigDecimal.ZERO) < 0) {
            estimatedProfit = BigDecimal.ZERO;
        }

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalRevenue", totalRevenue);
        metrics.put("estimatedCost", estimatedCost);
        metrics.put("estimatedProfit", estimatedProfit);
        metrics.put("estimatedLoss", estimatedLoss);
        metrics.put("totalOrders", orders.size());
        metrics.put("totalBuyers", buyerCount);
        metrics.put("totalSellers", sellerCount);
        metrics.put("salesByCategory", salesByCategory);
        return metrics;
    }
}
