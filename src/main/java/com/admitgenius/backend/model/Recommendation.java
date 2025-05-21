package com.admitgenius.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "recommendations")
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User student;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private String modelVersion;
    
    @Column(columnDefinition = "TEXT")
    private String inputSummary;
    
    @Enumerated(EnumType.STRING)
    private RecommendationType recommendationType;
    
    @OneToMany(mappedBy = "recommendation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecommendationItem> items = new ArrayList<>();
    
    public enum RecommendationType {
        SCHOOL, PROGRAM, ESSAY_STYLE
    }
    
    // 辅助方法
    public void addItem(RecommendationItem item) {
        items.add(item);
        item.setRecommendation(this);
    }
    
    public String generateExplanation() {
        StringBuilder explanation = new StringBuilder();
        explanation.append("根据您的学术背景和兴趣，我们为您推荐了 ")
                  .append(items.size())
                  .append(" 所院校。");
                  
        if (!items.isEmpty()) {
            RecommendationItem topItem = items.get(0);
            explanation.append("其中，")
                      .append(topItem.getSchool().getName())
                      .append(" 与您的背景匹配度最高 (")
                      .append(String.format("%.2f", topItem.getMatchScore() * 100))
                      .append("%)。");
        }
        
        return explanation.toString();
    }
    
    // 添加显式的getter和setter方法
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getInputSummary() {
        return inputSummary;
    }

    public void setInputSummary(String inputSummary) {
        this.inputSummary = inputSummary;
    }

    public RecommendationType getRecommendationType() {
        return recommendationType;
    }

    public void setRecommendationType(RecommendationType recommendationType) {
        this.recommendationType = recommendationType;
    }

    public List<RecommendationItem> getItems() {
        return items;
    }

    public void setItems(List<RecommendationItem> items) {
        this.items = items;
    }
} 