package com.admitgenius.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "expert_reviews")
public class ExpertReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "expert_id")
    private User expert;
    
    @Enumerated(EnumType.STRING)
    private ReviewTargetType targetType;
    
    private Long targetId; // 评审对象ID (学校、文书、要求等)
    
    private Integer rating; // 1-5星评分
    
    @Column(columnDefinition = "TEXT")
    private String comment;
    
    private Boolean approved = false;
    
    private LocalDateTime reviewedAt = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    private VisibilityLevel visibilityLevel = VisibilityLevel.STUDENT;
    
    public enum ReviewTargetType {
        SCHOOL, ESSAY, REQUIREMENT
    }
    
    public enum VisibilityLevel {
        PRIVATE, STUDENT, PUBLIC
    }
    
    // 判断对指定用户是否可见
    public boolean isVisible(User user) {
        if (this.visibilityLevel == VisibilityLevel.PUBLIC) {
            return true;
        }
        
        if (this.visibilityLevel == VisibilityLevel.STUDENT && 
            this.getTargetObject() instanceof Essay) {
            Essay essay = (Essay) this.getTargetObject();
            return essay.getUser().getId().equals(user.getId());
        }
        
        return false;
    }
    
    // 获取评审目标对象
    public Object getTargetObject() {
        // 实际实现中，应该根据targetType和targetId从数据库获取相应对象
        // 这里仅为示例
        return null;
    }
    
    // 审批评审
    public void approve() {
        this.approved = true;
    }
    
    // 拒绝评审
    public void reject() {
        this.approved = false;
    }
} 