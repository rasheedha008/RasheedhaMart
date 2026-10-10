package com.rasheedha.rasheedhamart.listener;

import com.rasheedha.rasheedhamart.dao.DatabaseManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DatabaseConnectionListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Initializing HikariCP Database Pool via DatabaseConnectionListener...");
        String dbUrl = System.getenv("DB_URL");
        DatabaseManager.initialize(dbUrl != null ? dbUrl : "jdbc:h2:mem:rasheedhadb;DB_CLOSE_DELAY=-1");
        sce.getServletContext().setAttribute("dbInitialized", true);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Shutting down HikariCP Database Pool via DatabaseConnectionListener...");
        DatabaseManager.shutdown();
    }
}
