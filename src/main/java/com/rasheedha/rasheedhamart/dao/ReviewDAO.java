package com.rasheedha.rasheedhamart.dao;

import com.rasheedha.rasheedhamart.model.Review;
import java.util.List;

public interface ReviewDAO {
    Review create(Review review);
    List<Review> findByProductId(Long productId);
    double getAverageRating(Long productId);
    boolean hasPurchasedProduct(Long buyerId, Long productId);
}
