package com.rasheedha.rasheedhamart;

import com.rasheedha.rasheedhamart.dao.DatabaseManager;
import com.rasheedha.rasheedhamart.dao.UserDAO;
import com.rasheedha.rasheedhamart.dao.UserDAOImpl;
import com.rasheedha.rasheedhamart.exception.AuthenticationException;
import com.rasheedha.rasheedhamart.model.User;
import com.rasheedha.rasheedhamart.service.AuthService;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdminAuthTest {

    private UserDAO userDAO;
    private AuthService authService;

    @BeforeAll
    void setupDatabase() {
        DatabaseManager.initialize("jdbc:h2:mem:adminauthtestdb;DB_CLOSE_DELAY=-1");
        userDAO = new UserDAOImpl();
        authService = new AuthService(userDAO);
    }

    @AfterAll
    void tearDown() {
        DatabaseManager.shutdown();
    }

    @Test
    void testValidAdminAuthentication() {
        User admin1 = authService.adminLogin("RasheedhaMartAdmin@gmail.com", "rasheedhamart00");
        assertNotNull(admin1);
        assertEquals("ADMIN", admin1.getRole());
        assertEquals("rasheedhamartadmin@gmail.com", admin1.getEmail().toLowerCase());

        User admin2 = authService.adminLogin("Rasheedhamart_admin@gmail.com", "rasheedhamart00");
        assertNotNull(admin2);
        assertEquals("ADMIN", admin2.getRole());

        User adminViaLogin = authService.login("RasheedhaMartAdmin@gmail.com", "rasheedhamart00");
        assertNotNull(adminViaLogin);
        assertEquals("ADMIN", adminViaLogin.getRole());
    }

    @Test
    void testInvalidAdminCredentialsRejected() {
        AuthenticationException ex1 = assertThrows(AuthenticationException.class, () -> {
            authService.adminLogin("RasheedhaMartAdmin@gmail.com", "wrongpassword");
        });
        assertEquals("Invalid admin email or password.", ex1.getMessage());

        AuthenticationException ex2 = assertThrows(AuthenticationException.class, () -> {
            authService.adminLogin("unknown@gmail.com", "rasheedhamart00");
        });
        assertEquals("Invalid admin email or password.", ex2.getMessage());
    }

    @Test
    void testBuyerCannotAuthenticateAsAdmin() {
        AuthenticationException ex = assertThrows(AuthenticationException.class, () -> {
            authService.adminLogin("buyer.john@gmail.com", "password123");
        });
        assertEquals("Invalid admin email or password.", ex.getMessage());
    }
}
