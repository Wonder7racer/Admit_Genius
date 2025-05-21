package com.admitgenius.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "essays")
public class Essay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //private Integer version;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    
    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private EssayType essayType = EssayType.PERSONAL_STATEMENT;

    public enum EssayType {
        PERSONAL_STATEMENT, STATEMENT_OF_PURPOSE, DIVERSITY_STATEMENT, 
        WHY_THIS_SCHOOL, EXTRACURRICULAR_ACTIVITY, CHALLENGE, LEADERSHIP,
        FUTURE_GOALS, SUPPLEMENTAL, OTHER
    }
    
    @Column(columnDefinition = "TEXT")
    private String content;


    @Enumerated(EnumType.STRING)
    private GenerationSource generatedBy = GenerationSource.STUDENT;

    public enum GenerationSource {
        AI_MODEL, STUDENT
    }

    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    
    
    // 手动添加getter和setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    

    // public Integer getVersion() {
    //     return version;
    // }

    // public void setVersion(Integer version) {
    //     this.version = version;
    // }
    
    public EssayType getEssayType() {
        return essayType;
    }
    
    public void setEssayType(EssayType essayType) {
        this.essayType = essayType;
    }
    
 
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public GenerationSource getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(GenerationSource generatedBy) {
        this.generatedBy = generatedBy;
    }
} 