package com.admitgenius.backend.dto;

import lombok.Data;
import java.util.List;

/**
 * 文书润色DTO
 * 参照文档5.5.1中定义的文书润色请求体结构
 */
@Data
public class EssayPolishDTO {
    // 基本信息
    private Long essayId;          // 文书ID（对已有文书润色）
    private Long userId;           // 用户ID

    
    // 润色选项
    private Boolean improveStructure = true; // 是否改进结构
    private Boolean enhanceLanguage = true;  // 是否提升语言表达
    private Boolean checkGrammar = true;     // 是否检查语法
    private Boolean reduceClichés = true;    // 是否减少陈词滥调
    private Boolean addExamples = false;     // 是否添加例子（可能扩展内容）
    private String tonePreference;     // 语言风格偏好（如正式、专业、个人化等）
    
    // 内容指导
    private List<String> focusPoints;  // 希望润色重点关注的几个方面
    private List<String> avoidPoints;  // 希望避免或减少的内容

    private String customDescription; // 自定义描述（如润色的具体要求或期望）

    

    // 格式要求
    private Integer targetWordCount;   // 目标字数
    


    
    // 手动添加getter和setter方法
    public Long getEssayId() {
        return essayId;
    }
    
    public void setEssayId(Long essayId) {
        this.essayId = essayId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    
    public Boolean getImproveStructure() {
        return improveStructure;
    }
    
    public void setImproveStructure(Boolean improveStructure) {
        this.improveStructure = improveStructure;
    }
    
    public Boolean getEnhanceLanguage() {
        return enhanceLanguage;
    }
    
    public void setEnhanceLanguage(Boolean enhanceLanguage) {
        this.enhanceLanguage = enhanceLanguage;
    }
    
    public Boolean getCheckGrammar() {
        return checkGrammar;
    }
    
    public void setCheckGrammar(Boolean checkGrammar) {
        this.checkGrammar = checkGrammar;
    }
    
    public Boolean getReduceClichés() {
        return reduceClichés;
    }
    
    public void setReduceClichés(Boolean reduceClichés) {
        this.reduceClichés = reduceClichés;
    }
    
    public Boolean getAddExamples() {
        return addExamples;
    }
    
    public void setAddExamples(Boolean addExamples) {
        this.addExamples = addExamples;
    }
    
    public List<String> getFocusPoints() {
        return focusPoints;
    }
    
    public void setFocusPoints(List<String> focusPoints) {
        this.focusPoints = focusPoints;
    }
    
    public List<String> getAvoidPoints() {
        return avoidPoints;
    }
    
    public void setAvoidPoints(List<String> avoidPoints) {
        this.avoidPoints = avoidPoints;
    }
    

    
    public Integer getTargetWordCount() {
        return targetWordCount;
    }
    
    public void setTargetWordCount(Integer targetWordCount) {
        this.targetWordCount = targetWordCount;
    }
    
    public String getTonePreference() {
        return tonePreference;
    }
    
    public void setTonePreference(String tonePreference) {
        this.tonePreference = tonePreference;
    }

    public String getCustomDescription() {
        return customDescription;
    }

    public void setCustomDescription(String customDescription) {
        this.customDescription = customDescription;
    }
} 