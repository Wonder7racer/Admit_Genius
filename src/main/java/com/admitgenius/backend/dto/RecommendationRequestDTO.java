package com.admitgenius.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * 推荐请求DTO
 * 参照文档5.6.1中定义的生成推荐请求体结构
 */
@Data
public class RecommendationRequestDTO {
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    // 学术信息
    @DecimalMin(value = "0.0", inclusive = true, message = "GPA不能小于0")
    @DecimalMax(value = "4.0", inclusive = true, message = "GPA不能大于4.0")
    private Double gpa;
    
    @Size(max = 255, message = "本科院校名称长度不能超过255个字符")
    private String undergraduateSchool;
    
    // 语言和考试成绩
    @PositiveOrZero(message = "托福分数不能为负")
    private Integer toeflScore;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "雅思分数不能小于0")
    private Double ieltsScore;
    
    @PositiveOrZero(message = "GRE总分不能为负")
    private Integer greScore;
    
    @PositiveOrZero(message = "GRE语文分数不能为负")
    private Integer greVerbal;
    
    @PositiveOrZero(message = "GRE数学分数不能为负")
    private Integer greQuantitative;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "GRE分析性写作分数不能小于0")
    private Double greAnalytical;
    
    @PositiveOrZero(message = "GMAT分数不能为负")
    private Integer gmatScore;
    
    // 目标专业和偏好
    @Size(max = 255, message = "目标专业长度不能超过255个字符")
    private String targetMajor;
    
    @Size(max = 50, message = "目标学位长度不能超过50个字符")
    private String targetDegree;
    
    private List<String> locationPreferences = new ArrayList<>();
    private List<String> schoolTypePreferences = new ArrayList<>();
    
    // 推荐类型和数量
    @NotBlank(message = "推荐类型不能为空")
    private String recommendationType = "SCHOOL"; // SCHOOL, PROGRAM
    
    @NotNull(message = "推荐数量不能为空")
    @Min(value = 1, message = "推荐数量至少为1")
    @Max(value = 20, message = "推荐数量最多为20")
    private Integer count = 5; // 建议返回的推荐数量
    
    // 兴趣和经历权重
    private Integer researchWeight = 3;
    private Integer internshipWeight = 3;
    private Integer volunteeringWeight = 3;
    private Integer leadershipWeight = 3;
    private Integer publicationWeight = 3;
    
    // 个人经历摘要（用于匹配）
    private String researchExperience;
    private String internshipExperience;
    private String volunteeringExperience;
    private String leadershipExperience;
    private String publications;
    
    // 自定义高级选项
    private Boolean includeScholarshipInfo = true;
    private Boolean includeTuitionInfo = true;
    private Boolean includeAdmissionStatistics = true;
    private Boolean includeMatchExplanation = true;
    private Boolean includeFacultyInfo = false;
    
    // 添加显式的getter和setter方法
    
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }
    
    public String getUndergraduateSchool() {
        return undergraduateSchool;
    }
    
    public void setUndergraduateSchool(String undergraduateSchool) {
        this.undergraduateSchool = undergraduateSchool;
    }
    
    public Integer getToeflScore() {
        return toeflScore;
    }
    
    public void setToeflScore(Integer toeflScore) {
        this.toeflScore = toeflScore;
    }
    
    public Double getIeltsScore() {
        return ieltsScore;
    }
    
    public void setIeltsScore(Double ieltsScore) {
        this.ieltsScore = ieltsScore;
    }
    
    public Integer getGreScore() {
        return greScore;
    }
    
    public void setGreScore(Integer greScore) {
        this.greScore = greScore;
    }
    
    public Integer getGreVerbal() {
        return greVerbal;
    }
    
    public void setGreVerbal(Integer greVerbal) {
        this.greVerbal = greVerbal;
    }
    
    public Integer getGreQuantitative() {
        return greQuantitative;
    }
    
    public void setGreQuantitative(Integer greQuantitative) {
        this.greQuantitative = greQuantitative;
    }
    
    public Double getGreAnalytical() {
        return greAnalytical;
    }
    
    public void setGreAnalytical(Double greAnalytical) {
        this.greAnalytical = greAnalytical;
    }
    
    public Integer getGmatScore() {
        return gmatScore;
    }
    
    public void setGmatScore(Integer gmatScore) {
        this.gmatScore = gmatScore;
    }

    public String getTargetMajor() {
        return targetMajor;
    }

    public void setTargetMajor(String targetMajor) {
        this.targetMajor = targetMajor;
    }
    
    public String getTargetDegree() {
        return targetDegree;
    }
    
    public void setTargetDegree(String targetDegree) {
        this.targetDegree = targetDegree;
    }

    public List<String> getLocationPreferences() {
        return locationPreferences;
    }

    public void setLocationPreferences(List<String> locationPreferences) {
        this.locationPreferences = locationPreferences;
    }

    public List<String> getSchoolTypePreferences() {
        return schoolTypePreferences;
    }

    public void setSchoolTypePreferences(List<String> schoolTypePreferences) {
        this.schoolTypePreferences = schoolTypePreferences;
    }

    public String getRecommendationType() {
        return recommendationType;
    }

    public void setRecommendationType(String recommendationType) {
        this.recommendationType = recommendationType;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
    
    public Integer getResearchWeight() {
        return researchWeight;
    }
    
    public void setResearchWeight(Integer researchWeight) {
        this.researchWeight = researchWeight;
    }
    
    public Integer getInternshipWeight() {
        return internshipWeight;
    }
    
    public void setInternshipWeight(Integer internshipWeight) {
        this.internshipWeight = internshipWeight;
    }
    
    public Integer getVolunteeringWeight() {
        return volunteeringWeight;
    }
    
    public void setVolunteeringWeight(Integer volunteeringWeight) {
        this.volunteeringWeight = volunteeringWeight;
    }
    
    public Integer getLeadershipWeight() {
        return leadershipWeight;
    }
    
    public void setLeadershipWeight(Integer leadershipWeight) {
        this.leadershipWeight = leadershipWeight;
    }
    
    public Integer getPublicationWeight() {
        return publicationWeight;
    }
    
    public void setPublicationWeight(Integer publicationWeight) {
        this.publicationWeight = publicationWeight;
    }
    
    public String getResearchExperience() {
        return researchExperience;
    }
    
    public void setResearchExperience(String researchExperience) {
        this.researchExperience = researchExperience;
    }
    
    public String getInternshipExperience() {
        return internshipExperience;
    }
    
    public void setInternshipExperience(String internshipExperience) {
        this.internshipExperience = internshipExperience;
    }
    
    public String getVolunteeringExperience() {
        return volunteeringExperience;
    }
    
    public void setVolunteeringExperience(String volunteeringExperience) {
        this.volunteeringExperience = volunteeringExperience;
    }
    
    public String getLeadershipExperience() {
        return leadershipExperience;
    }
    
    public void setLeadershipExperience(String leadershipExperience) {
        this.leadershipExperience = leadershipExperience;
    }
    
    public String getPublications() {
        return publications;
    }
    
    public void setPublications(String publications) {
        this.publications = publications;
    }
    
    public Boolean getIncludeScholarshipInfo() {
        return includeScholarshipInfo;
    }
    
    public void setIncludeScholarshipInfo(Boolean includeScholarshipInfo) {
        this.includeScholarshipInfo = includeScholarshipInfo;
    }
    
    public Boolean getIncludeTuitionInfo() {
        return includeTuitionInfo;
    }
    
    public void setIncludeTuitionInfo(Boolean includeTuitionInfo) {
        this.includeTuitionInfo = includeTuitionInfo;
    }
    
    public Boolean getIncludeAdmissionStatistics() {
        return includeAdmissionStatistics;
    }
    
    public void setIncludeAdmissionStatistics(Boolean includeAdmissionStatistics) {
        this.includeAdmissionStatistics = includeAdmissionStatistics;
    }
    
    public Boolean getIncludeMatchExplanation() {
        return includeMatchExplanation;
    }
    
    public void setIncludeMatchExplanation(Boolean includeMatchExplanation) {
        this.includeMatchExplanation = includeMatchExplanation;
    }
    
    public Boolean getIncludeFacultyInfo() {
        return includeFacultyInfo;
    }
    
    public void setIncludeFacultyInfo(Boolean includeFacultyInfo) {
        this.includeFacultyInfo = includeFacultyInfo;
    }
} 