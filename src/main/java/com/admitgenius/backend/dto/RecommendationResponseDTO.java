package com.admitgenius.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class RecommendationResponseDTO {
    private Long id;
    private Long userId;
    private LocalDateTime createdAt;
    private String explanation;
    private List<RecommendationItemDTO> items = new ArrayList<>();
    private Map<String, Object> statistics;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public List<RecommendationItemDTO> getItems() {
        return items;
    }

    public void setItems(List<RecommendationItemDTO> items) {
        this.items = items;
    }

    public Map<String, Object> getStatistics() {
        return statistics;
    }

    public void setStatistics(Map<String, Object> statistics) {
        this.statistics = statistics;
    }
    
    @Data
    public static class RecommendationItemDTO {
        private Long id;
        private Long schoolId;
        private String schoolName;
        private String schoolLocation;
        private String schoolLogo;
        private Integer schoolRanking;
        
        private Long programId;
        private String programName;
        private String department;
        private String degreeLevel;
        
        private Float matchScore;
        private Integer rank;
        private String matchReason;
        
        // 可选详细信息
        private Double tuitionFee;
        private Boolean scholarshipAvailable;
        private Map<String, Object> admissionStats;
        
        // 学生反馈相关
        private Boolean isApplied;
        private String feedback;
        
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(Long schoolId) {
            this.schoolId = schoolId;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public Float getMatchScore() {
            return matchScore;
        }

        public void setMatchScore(Float matchScore) {
            this.matchScore = matchScore;
        }

        public String getMatchReason() {
            return matchReason;
        }

        public void setMatchReason(String matchReason) {
            this.matchReason = matchReason;
        }

        public Boolean getIsApplied() {
            return isApplied;
        }

        public void setIsApplied(Boolean isApplied) {
            this.isApplied = isApplied;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }
        
        // 辅助方法
        public String getFormattedMatchScore() {
            return String.format("%.1f%%", matchScore * 100);
        }
    }
    
    // 汇总统计信息
    @Data
    public static class RecommendationStats {
        private Double avgRanking;
        private Double avgAcceptanceRate;
        private Double avgTuition;
        private Integer ivyLeagueCount;
        private Map<String, Integer> locationDistribution;
        private List<String> topDepartments;
        
        public Double getAvgRanking() {
            return avgRanking;
        }

        public void setAvgRanking(Double avgRanking) {
            this.avgRanking = avgRanking;
        }

        public Double getAvgAcceptanceRate() {
            return avgAcceptanceRate;
        }

        public void setAvgAcceptanceRate(Double avgAcceptanceRate) {
            this.avgAcceptanceRate = avgAcceptanceRate;
        }

        public Double getAvgTuition() {
            return avgTuition;
        }

        public void setAvgTuition(Double avgTuition) {
            this.avgTuition = avgTuition;
        }

        public Integer getIvyLeagueCount() {
            return ivyLeagueCount;
        }

        public void setIvyLeagueCount(Integer ivyLeagueCount) {
            this.ivyLeagueCount = ivyLeagueCount;
        }

        public Map<String, Integer> getLocationDistribution() {
            return locationDistribution;
        }

        public void setLocationDistribution(Map<String, Integer> locationDistribution) {
            this.locationDistribution = locationDistribution;
        }

        public List<String> getTopDepartments() {
            return topDepartments;
        }

        public void setTopDepartments(List<String> topDepartments) {
            this.topDepartments = topDepartments;
        }
    }
} 