package com.rasheedha.rasheedhamart.service;

import com.rasheedha.rasheedhamart.dao.UserDAO;
import com.rasheedha.rasheedhamart.dto.UserResponseDTO;
import com.rasheedha.rasheedhamart.exception.AuthenticationException;
import com.rasheedha.rasheedhamart.exception.ValidationException;
import com.rasheedha.rasheedhamart.model.User;
import com.rasheedha.rasheedhamart.util.PasswordUtil;
import com.rasheedha.rasheedhamart.util.ValidationUtil;

import java.util.Optional;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserResponseDTO register(String email, String password, String fullName, String role) {
        ValidationUtil.validateEmail(email);
        ValidationUtil.validateNotBlank(password, "Password");
        ValidationUtil.validateNotBlank(fullName, "Full Name");

        if (!"BUYER".equalsIgnoreCase(role) && !"SELLER".equalsIgnoreCase(role)) {
            throw new ValidationException("Only BUYER or SELLER role registration is permitted.");
        }

        if (userDAO.findByEmail(email.trim().toLowerCase()).isPresent()) {
            throw new ValidationException("An account with email " + email + " already exists.");
        }

        String hashedPassword = PasswordUtil.hashPassword(password);
        User user = new User(null, email.trim().toLowerCase(), hashedPassword, fullName.trim(), role.toUpperCase(), null);
        User created = userDAO.create(user);
        return UserResponseDTO.fromUser(created);
    }

    public User login(String email, String password) {
        ValidationUtil.validateEmail(email);
        ValidationUtil.validateNotBlank(password, "Password");

        Optional<User> userOpt = userDAO.findByEmail(email.trim().toLowerCase());
        if (userOpt.isEmpty()) {
            throw new AuthenticationException("Invalid email or password.");
        }

        User user = userOpt.get();
        if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            throw new AuthenticationException("Invalid email or password.");
        }

        return user;
    }
}
