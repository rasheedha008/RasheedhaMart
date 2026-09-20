package com.rasheedha.rasheedhamart.util;

import com.rasheedha.rasheedhamart.exception.ValidationException;
import java.math.BigDecimal;
import java.util.regex.Pattern;

public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    public static void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new ValidationException("Invalid email address format.");
        }
    }

    public static void validateNotBlank(String field, String fieldName) {
        if (field == null || field.trim().isEmpty()) {
            throw new ValidationException(fieldName + " cannot be empty.");
        }
    }

    public static void validatePositivePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Price must be greater than zero.");
        }
    }

    public static void validateNonNegativeStock(int stock) {
        if (stock < 0) {
            throw new ValidationException("Stock quantity cannot be negative.");
        }
    }

    public static void validateRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new ValidationException("Star rating must be between 1 and 5.");
        }
    }
}
