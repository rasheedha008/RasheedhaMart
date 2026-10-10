package com.rasheedha.rasheedhamart.chatbot;

import com.rasheedha.rasheedhamart.dao.ProductDAO;
import com.rasheedha.rasheedhamart.dao.ProductDAOImpl;
import com.rasheedha.rasheedhamart.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MockChatProvider implements ChatProvider {

    private final ProductDAO productDAO;
    private final Map<String, String> faqResponses = new HashMap<>();

    public MockChatProvider() {
        this.productDAO = new ProductDAOImpl();
        faqResponses.put("shipping", "RasheedhaMart provides Free Express Shipping on all orders! Standard delivery takes 2-4 business days.");
        faqResponses.put("delivery", "You can track your order status live in your 'My Orders' dashboard (Pending -> Confirmed -> Shipped -> Delivered).");
        faqResponses.put("payment", "We support seamless mock payment confirmation during checkout with instant order generation!");
        faqResponses.put("return", "Hassle-free 7-day return policy is available for all delivered items through customer support.");
        faqResponses.put("seller", "Sellers can manage listings, inspect stock levels, and process incoming orders directly from the Seller Dashboard.");
        faqResponses.put("review", "Buyers can submit 1 to 5 star ratings and reviews on products they have successfully purchased and received.");
        faqResponses.put("contact", "Customer support is available 24/7 at support@rasheedhamart.com or via phone at +91 9876543210.");
    }

    public MockChatProvider(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public String getResponse(String userMessage, String userRole) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "Hello! How can I assist you with your RasheedhaMart shopping experience today?";
        }

        String msg = userMessage.toLowerCase().trim();

        // Domain Guard
        if (msg.contains("weather") || msg.contains("politics") || msg.contains("math") || msg.contains("code") || msg.contains("recipe")) {
            return "I am RasheedhaMart AI Assistant. I can only help you with products, orders, carting, seller tools, and platform queries!";
        }

        // Smart Catalog & Price / Stock Queries
        Pattern pricePattern = Pattern.compile("(under|below|less than|within|max|around|up to)?\\s*₹?\\s*(\\d+)\\s*(rupees|rs|inr|rupee)?");
        Matcher priceMatcher = pricePattern.matcher(msg);
        Double parsedPrice = null;
        if (priceMatcher.find() && priceMatcher.group(2) != null) {
            try {
                parsedPrice = Double.parseDouble(priceMatcher.group(2));
            } catch (NumberFormatException ignored) {}
        }

        String parsedCat = null;
        if (msg.contains("electronic") || msg.contains("gadget")) parsedCat = "Electronics";
        else if (msg.contains("fashion") || msg.contains("saree") || msg.contains("cloth")) parsedCat = "Fashion";
        else if (msg.contains("grocer") || msg.contains("spice") || msg.contains("food")) parsedCat = "Groceries";
        else if (msg.contains("home") || msg.contains("living") || msg.contains("bottle")) parsedCat = "Home & Living";

        try {
            List<Product> allProducts = productDAO.search(null, null, 0, 100);
            List<Product> matching = new ArrayList<>();

            for (Product p : allProducts) {
                if (p.isHidden()) continue;
                if (parsedCat != null && !p.getCategory().equalsIgnoreCase(parsedCat)) continue;
                if (parsedPrice != null && p.getPrice().doubleValue() > parsedPrice) continue;
                matching.add(p);
            }

            if (parsedPrice != null || parsedCat != null || msg.contains("product") || msg.contains("available") || msg.contains("stock") || msg.contains("item")) {
                if (matching.isEmpty()) {
                    String categoryStr = parsedCat != null ? "in category '" + parsedCat + "' " : "";
                    String priceStr = parsedPrice != null ? "under ₹" + String.format("%.0f", parsedPrice) : "";
                    return "No active products found matching your search " + categoryStr + priceStr + ". Please check our full storefront catalog!";
                }

                StringBuilder sb = new StringBuilder();
                sb.append("Found ").append(matching.size()).append(" product(s)");
                if (parsedCat != null) sb.append(" in '").append(parsedCat).append("'");
                if (parsedPrice != null) sb.append(" under ₹").append(String.format("%.0f", parsedPrice));
                sb.append(":\n");

                for (Product p : matching) {
                    sb.append("• ").append(p.getName())
                      .append(" — Category: ").append(p.getCategory())
                      .append(", Price: ₹").append(String.format("%.2f", p.getPrice()))
                      .append(", Stock: ").append(p.getStockQuantity()).append(" left\n");
                }
                return sb.toString().trim();
            }
        } catch (Exception e) {
            // Fallback to FAQ matching if database query fails
        }

        for (Map.Entry<String, String> entry : faqResponses.entrySet()) {
            if (msg.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return "Welcome to RasheedhaMart! You can browse our products catalog, filter by category or price, add items to cart, select delivery addresses, and track order progress live.";
    }
}
