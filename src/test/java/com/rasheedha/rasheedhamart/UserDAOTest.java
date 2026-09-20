package com.rasheedha.rasheedhamart;

import com.rasheedha.rasheedhamart.dao.DatabaseManager;
import com.rasheedha.rasheedhamart.dao.UserDAO;
import com.rasheedha.rasheedhamart.dao.UserDAOImpl;
import com.rasheedha.rasheedhamart.model.User;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDAOTest {

    private UserDAO userDAO;

    @BeforeAll
    void setupDatabase() {
        DatabaseManager.initialize("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        userDAO = new UserDAOImpl();
    }

    @AfterAll
    void tearDown() {
        DatabaseManager.shutdown();
    }

    @Test
    void testCreateAndFindUser() {
        User user = new User(null, "test.buyer@rasheedha.com", "$2a$10$76g.p5nS7uS8", "Test Buyer", "BUYER", null);
        User created = userDAO.create(user);

        assertNotNull(created.getId());
        Optional<User> found = userDAO.findByEmail("test.buyer@rasheedha.com");
        assertTrue(found.isPresent());
        assertEquals("Test Buyer", found.get().getFullName());
        assertEquals("BUYER", found.get().getRole());
    }
}
