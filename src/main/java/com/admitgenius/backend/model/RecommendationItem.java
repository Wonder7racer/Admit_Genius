package com.admitgenius.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "recommendation_items")
public class RecommendationItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "recommendation_id")
    private Recommendation recommendation;
    
    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;
    
    @ManyToOne
    @JoinColumn(name = "program_id")
    private SchoolProgram program;
    
    private Float matchScore; // 匹配度分数，0-1之间
    
    private Integer rank; // 在推荐列表中的排名
    
    @Column(columnDefinition = "TEXT")
    private String matchReason; // 匹配原因说明
    
    @Column(columnDefinition = "TEXT")
    private String feedback; // 用户对推荐的反馈
    
    private Boolean isApplied = false; // 用户是否已申请该校
    
    // 标记为已申请
    public void markAsApplied() {
        this.isApplied = true;
    }
    
    // 提供反馈
    public void provideFeedback(String feedback) {
        this.feedback = feedback;
    }
    
    // 添加显式的getter和setter方法
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Recommendation getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public SchoolProgram getProgram() {
        return program;
    }

    public void setProgram(SchoolProgram program) {
        this.program = program;
    }

    public Float getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(Float matchScore) {
        this.matchScore = matchScore;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getMatchReason() {
        return matchReason;
    }

    public void setMatchReason(String matchReason) {
        this.matchReason = matchReason;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Boolean getIsApplied() {
        return isApplied;
    }

    public void setIsApplied(Boolean isApplied) {
        this.isApplied = isApplied;
    }
} 