package com.rasheedha.rasheedhamart.service;

import com.rasheedha.rasheedhamart.dao.PromotionDAO;
import com.rasheedha.rasheedhamart.exception.ValidationException;
import com.rasheedha.rasheedhamart.model.Promotion;
import com.rasheedha.rasheedhamart.util.ValidationUtil;
import com.rasheedha.rasheedhamart.util.XSSUtil;

import java.util.List;

public class MarketingService {

    private final PromotionDAO promotionDAO;

    public MarketingService(PromotionDAO promotionDAO) {
        this.promotionDAO = promotionDAO;
    }

    public Promotion createPromotion(Promotion promotion) {
        ValidationUtil.validateNotBlank(promotion.getTitle(), "Promotion Title");
        promotion.setTitle(XSSUtil.escapeHtml(promotion.getTitle()));
        promotion.setDescription(XSSUtil.escapeHtml(promotion.getDescription()));
        return promotionDAO.create(promotion);
    }

    public List<Promotion> getAllPromotions() {
        return promotionDAO.findAll();
    }

    public List<Promotion> getActivePromotions() {
        return promotionDAO.findActivePromotions();
    }

    public boolean deletePromotion(Long id) {
        return promotionDAO.delete(id);
    }
}
