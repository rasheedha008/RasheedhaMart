package com.rasheedha.rasheedhamart.server;

import com.rasheedha.rasheedhamart.controller.*;
import com.rasheedha.rasheedhamart.dao.*;
import com.rasheedha.rasheedhamart.filter.AuthFilter;
import com.rasheedha.rasheedhamart.filter.EncodingFilter;
import com.rasheedha.rasheedhamart.listener.DatabaseConnectionListener;
import com.rasheedha.rasheedhamart.service.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class MainServer {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        String envPort = System.getenv("PORT");
        if (envPort != null && !envPort.trim().isEmpty()) {
            try { port = Integer.parseInt(envPort.trim()); } catch (Exception ignored) {}
        } else if (args.length > 0) {
            try { port = Integer.parseInt(args[0]); } catch (Exception ignored) {}
        }

        System.out.println("==================================================");
        System.out.println(" Starting RasheedhaMart Enterprise Platform...");
        System.out.println(" Bounded Port: " + port);
        System.out.println("==================================================");

        // 1. Initialize Database Pool & Schemas
        DatabaseManager.initialize("jdbc:h2:mem:rasheedhadb;DB_CLOSE_DELAY=-1");

        // 2. Instantiate DAOs
        UserDAO userDAO = new UserDAOImpl();
        ProductDAO productDAO = new ProductDAOImpl();
        CartDAO cartDAO = new CartDAOImpl();
        OrderDAO orderDAO = new OrderDAOImpl();
        ReviewDAO reviewDAO = new ReviewDAOImpl();
        AddressDAO addressDAO = new AddressDAOImpl();
        PromotionDAO promotionDAO = new PromotionDAOImpl();

        // 3. Instantiate Services
        AuthService authService = new AuthService(userDAO);
        ProductService productService = new ProductService(productDAO);
        CartService cartService = new CartService(cartDAO, productDAO);
        OrderService orderService = new OrderService(orderDAO, cartDAO);
        ReviewService reviewService = new ReviewService(reviewDAO);
        AdminService adminService = new AdminService(userDAO, productDAO, orderDAO);
        AddressService addressService = new AddressService(addressDAO);
        MarketingService marketingService = new MarketingService(promotionDAO);
        AnalyticsService analyticsService = new AnalyticsService(userDAO, productDAO, orderDAO);

        // 4. Configure Jetty Servlet Context
        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addEventListener(new DatabaseConnectionListener());

        // Configure static web resource base if available
        String resourceBase = MainServer.class.getResource("/web") != null ? 
            MainServer.class.getResource("/web").toExternalForm() : "./src/main/resources/web";
        context.setResourceBase(resourceBase);

        // Register Filters
        context.addFilter(new FilterHolder(new EncodingFilter()), "/*", EnumSet.of(DispatcherType.REQUEST));
        context.addFilter(new FilterHolder(new AuthFilter()), "/api/v1/*", EnumSet.of(DispatcherType.REQUEST));

        // Register Servlets
        context.addServlet(new ServletHolder(new AuthServlet(authService)), "/api/v1/auth/*");
        context.addServlet(new ServletHolder(new ProductServlet(productService)), "/api/v1/products/*");
        context.addServlet(new ServletHolder(new CartServlet(cartService)), "/api/v1/cart/*");
        context.addServlet(new ServletHolder(new CheckoutServlet(orderService)), "/api/v1/checkout/*");
        context.addServlet(new ServletHolder(new OrderServlet(orderService)), "/api/v1/orders/*");
        context.addServlet(new ServletHolder(new ReviewServlet(reviewService)), "/api/v1/reviews/*");
        context.addServlet(new ServletHolder(new AdminServlet(adminService)), "/api/v1/admin/*");
        context.addServlet(new ServletHolder(new AddressServlet(addressService)), "/api/v1/addresses/*");
        context.addServlet(new ServletHolder(new AdminMarketingServlet(marketingService)), "/api/v1/admin/promotions/*");
        context.addServlet(new ServletHolder(new AdminAnalyticsServlet(analyticsService)), "/api/v1/admin/analytics/*");
        context.addServlet(new ServletHolder(new ChatServlet()), "/api/v1/chat/*");
        context.addServlet(new ServletHolder(new HealthServlet()), "/api/v1/health");

        // Default Servlet for UI static web files
        ServletHolder defaultServlet = new ServletHolder("default", DefaultServlet.class);
        defaultServlet.setInitParameter("dirAllowed", "false");
        context.addServlet(defaultServlet, "/");

        server.setHandler(context);

        // Start Server
        server.start();
        System.out.println(">>> RasheedhaMart is running successfully!");
        System.out.println(">>> Web Portal URL: http://localhost:" + port);
        System.out.println(">>> Health Check:   http://localhost:" + port + "/api/v1/health");
        System.out.println("==================================================");

        server.join();
    }
}
