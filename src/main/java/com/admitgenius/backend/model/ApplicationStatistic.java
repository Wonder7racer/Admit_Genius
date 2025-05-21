package com.admitgenius.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "application_statistics")
public class ApplicationStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;
    
    private Integer year;
    
    private String term;
    
    private Integer totalApplicants;
    
    private Integer acceptedCount;
    
    private Integer rejectedCount;
    
    private Integer waitlistedCount;
    
    private Float avgGPA;
    
    private Integer avgSAT;
    
    private Integer avgTOEFL;
    
    private Float internationalPercentage;
    
    // 辅助方法
    public Float getAcceptanceRate() {
        if (totalApplicants == null || totalApplicants == 0 || acceptedCount == null) {
            return null;
        }
        return (float) acceptedCount / totalApplicants;
    }
    
    public Float getRejectionRate() {
        if (totalApplicants == null || totalApplicants == 0 || rejectedCount == null) {
            return null;
        }
        return (float) rejectedCount / totalApplicants;
    }
    
    public Float getWaitlistRate() {
        if (totalApplicants == null || totalApplicants == 0 || waitlistedCount == null) {
            return null;
        }
        return (float) waitlistedCount / totalApplicants;
    }
} 