package com.rasheedha.rasheedhamart.dao;

import com.rasheedha.rasheedhamart.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductDAO {
    Product create(Product product);
    Product update(Product product);
    boolean delete(Long id);
    Optional<Product> findById(Long id);
    List<Product> findBySellerId(Long sellerId);
    List<Product> search(String keyword, String category, int offset, int limit);
    int countSearch(String keyword, String category);
    List<String> findAllCategories();
}
