package com.admitgenius.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;

/**
 * 文书DTO
 * 参照文档5.3.1中定义的文书创建请求体结构
 */
@Data
public class EssayDTO {
    private Long id;

    @NotNull(message = "User ID cannot be null")
    private Long userId;         // 用户ID

    @NotNull(message = "Title cannot be null")
    private String title;        // 文书标题

    @NotNull(message = " content cannot be null")
    private String content;      // 文书内容

    
    //private Integer version;      // 文书版本
        
    
    @NotNull(message = "Essay type cannot be null")
    private String essayType;    // 文书类型（如个人陈述、研究计划等,需在model的枚举中）

    // PERSONAL_STATEMENT, STATEMENT_OF_PURPOSE, DIVERSITY_STATEMENT, 
    // WHY_THIS_SCHOOL, EXTRACURRICULAR_ACTIVITY, CHALLENGE, LEADERSHIP,
    // FUTURE_GOALS, SUPPLEMENTAL, OTHER

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    
    private String generatedBy;  // 生成来源（如AI模型、学生手动输入等,需在model的枚举中）

} 