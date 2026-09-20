package com.rasheedha.rasheedhamart.service;

import com.rasheedha.rasheedhamart.dao.ProductDAO;
import com.rasheedha.rasheedhamart.dto.ProductDTO;
import com.rasheedha.rasheedhamart.dto.ProductSearchDTO;
import com.rasheedha.rasheedhamart.exception.ResourceNotFoundException;
import com.rasheedha.rasheedhamart.exception.ValidationException;
import com.rasheedha.rasheedhamart.model.Product;
import com.rasheedha.rasheedhamart.util.ValidationUtil;
import com.rasheedha.rasheedhamart.util.XSSUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductService {

    private final ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Product createProduct(Long sellerId, ProductDTO dto) {
        ValidationUtil.validateNotBlank(dto.getName(), "Product Name");
        ValidationUtil.validatePositivePrice(dto.getPrice());
        ValidationUtil.validateNonNegativeStock(dto.getStockQuantity());
        ValidationUtil.validateNotBlank(dto.getCategory(), "Category");

        Product product = new Product(
            null,
            sellerId,
            XSSUtil.escapeHtml(dto.getName()),
            XSSUtil.escapeHtml(dto.getDescription()),
            dto.getPrice(),
            dto.getStockQuantity(),
            XSSUtil.escapeHtml(dto.getCategory()),
            dto.getImageUrl(),
            null
        );

        return productDAO.create(product);
    }

    public Product updateProduct(Long sellerId, ProductDTO dto) {
        if (dto.getId() == null) {
            throw new ValidationException("Product ID is required for update.");
        }

        Product existing = productDAO.findById(dto.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + dto.getId()));

        if (!existing.getSellerId().equals(sellerId)) {
            throw new ValidationException("You do not have permission to modify this product listing.");
        }

        ValidationUtil.validateNotBlank(dto.getName(), "Product Name");
        ValidationUtil.validatePositivePrice(dto.getPrice());
        ValidationUtil.validateNonNegativeStock(dto.getStockQuantity());
        ValidationUtil.validateNotBlank(dto.getCategory(), "Category");

        existing.setName(XSSUtil.escapeHtml(dto.getName()));
        existing.setDescription(XSSUtil.escapeHtml(dto.getDescription()));
        existing.setPrice(dto.getPrice());
        existing.setStockQuantity(dto.getStockQuantity());
        existing.setCategory(XSSUtil.escapeHtml(dto.getCategory()));
        existing.setImageUrl(dto.getImageUrl());

        return productDAO.update(existing);
    }

    public boolean deleteProduct(Long sellerId, Long productId) {
        Product existing = productDAO.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        if (!existing.getSellerId().equals(sellerId)) {
            throw new ValidationException("You do not have permission to delete this product listing.");
        }

        return productDAO.delete(productId);
    }

    public Product getById(Long id) {
        return productDAO.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    }

    public List<Product> getSellerProducts(Long sellerId) {
        return productDAO.findBySellerId(sellerId);
    }

    public Map<String, Object> searchProducts(ProductSearchDTO searchDTO) {
        List<Product> products = productDAO.search(
            searchDTO.getKeyword(),
            searchDTO.getCategory(),
            searchDTO.getOffset(),
            searchDTO.getPageSize()
        );

        int totalCount = productDAO.countSearch(searchDTO.getKeyword(), searchDTO.getCategory());
        int totalPages = (int) Math.ceil((double) totalCount / searchDTO.getPageSize());

        Map<String, Object> result = new HashMap<>();
        result.put("products", products);
        result.put("totalCount", totalCount);
        result.put("page", searchDTO.getPage());
        result.put("pageSize", searchDTO.getPageSize());
        result.put("totalPages", totalPages);
        return result;
    }

    public List<String> getCategories() {
        return productDAO.findAllCategories();
    }
}
