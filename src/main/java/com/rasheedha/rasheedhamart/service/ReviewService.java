package com.rasheedha.rasheedhamart.service;

import com.rasheedha.rasheedhamart.dao.ReviewDAO;
import com.rasheedha.rasheedhamart.exception.ValidationException;
import com.rasheedha.rasheedhamart.model.Review;
import com.rasheedha.rasheedhamart.util.ValidationUtil;
import com.rasheedha.rasheedhamart.util.XSSUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewService {

    private final ReviewDAO reviewDAO;

    public ReviewService(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    public Review addReview(Long buyerId, Long productId, int rating, String comment) {
        ValidationUtil.validateRating(rating);

        if (!reviewDAO.hasPurchasedProduct(buyerId, productId)) {
            throw new ValidationException("You can only submit reviews for products you have purchased.");
        }

        Review review = new Review(
            null,
            productId,
            buyerId,
            rating,
            XSSUtil.escapeHtml(comment),
            null
        );

        return reviewDAO.create(review);
    }

    public Map<String, Object> getProductReviewSummary(Long productId) {
        List<Review> reviews = reviewDAO.findByProductId(productId);
        double avgRating = reviewDAO.getAverageRating(productId);

        Map<String, Object> summary = new HashMap<>();
        summary.put("reviews", reviews);
        summary.put("averageRating", Math.round(avgRating * 10.0) / 10.0);
        summary.put("reviewCount", reviews.size());
        return summary;
    }
}
