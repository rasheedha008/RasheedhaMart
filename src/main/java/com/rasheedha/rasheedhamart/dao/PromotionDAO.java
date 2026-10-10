package com.rasheedha.rasheedhamart.dao;

import com.rasheedha.rasheedhamart.model.Promotion;
import java.util.List;

public interface PromotionDAO {
    Promotion create(Promotion promotion);
    Promotion update(Promotion promotion);
    boolean delete(Long id);
    List<Promotion> findAll();
    List<Promotion> findActivePromotions();
}
