package com.rasheedha.rasheedhamart.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseManager {

    private static HikariDataSource dataSource;

    public static synchronized void initialize(String jdbcUrl) {
        if (dataSource != null && !dataSource.isClosed()) {
            return;
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl != null ? jdbcUrl : "jdbc:h2:mem:rasheedhadb;DB_CLOSE_DELAY=-1");
        config.setDriverClassName("org.h2.Driver");
        config.setUsername("sa");
        config.setPassword("");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setPoolName("RasheedhaMartHikariCP");

        dataSource = new HikariDataSource(config);

        // Auto-run schema and seed scripts
        executeSqlScript("/schema.sql");
        executeSqlScript("/seed.sql");
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            initialize("jdbc:h2:mem:rasheedhadb;DB_CLOSE_DELAY=-1");
        }
        return dataSource.getConnection();
    }

    public static synchronized void shutdown() {
        if (dataSource != null) {
            if (!dataSource.isClosed()) {
                dataSource.close();
            }
            dataSource = null;
        }
    }

    private static void executeSqlScript(String resourcePath) {
        InputStream is = DatabaseManager.class.getResourceAsStream(resourcePath);
        if (is == null) {
            // Fallback: try loading from file system
            String relativePath = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;
            File file = new File(relativePath);
            if (file.exists()) {
                try {
                    is = new FileInputStream(file);
                } catch (Exception e) {
                    System.err.println("Could not open file: " + file.getAbsolutePath());
                }
            }
        }

        if (is == null) {
            System.err.println("Script resource " + resourcePath + " not found on classpath or filesystem.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is));
             Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            String content = reader.lines()
                .filter(line -> !line.trim().startsWith("--"))
                .collect(Collectors.joining("\n"));

            for (String sqlStatement : content.split(";")) {
                String trimmed = sqlStatement.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }
            System.out.println("Executed SQL Script successfully: " + resourcePath);
        } catch (Exception e) {
            System.err.println("Error executing SQL script " + resourcePath + ": " + e.getMessage());
        }
    }
}
