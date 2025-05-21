package com.admitgenius.backend.repository;

import com.admitgenius.backend.model.Recommendation;
import com.admitgenius.backend.model.RecommendationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationItemRepository extends JpaRepository<RecommendationItem, Long> {
    List<RecommendationItem> findByRecommendation(Recommendation recommendation);
    List<RecommendationItem> findByRecommendationOrderByRankAsc(Recommendation recommendation);
} 