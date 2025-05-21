package com.admitgenius.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "schools")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "学校名称不能为空")
    @Column(nullable = false)
    private String name;
    
    @Size(max = 255, message = "地点长度不能超过255个字符")
    private String location;
    
    @PositiveOrZero(message = "排名必须为非负整数")
    private Integer ranking;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "录取率不能小于0")
    @DecimalMax(value = "1.0", inclusive = true, message = "录取率不能大于1")
    private Double acceptanceRate;
    
    // 平均分数
    @PositiveOrZero(message = "平均GRE语文分数必须为非负整数")
    private Integer averageGREVerbal;
    
    @PositiveOrZero(message = "平均GRE数学分数必须为非负整数")
    private Integer averageGREQuant;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "平均GRE分析性写作分数不能小于0")
    private Double averageGREAW;
    
    @PositiveOrZero(message = "平均GMAT分数必须为非负整数")
    private Integer averageGMAT;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "平均GPA不能小于0")
    @DecimalMax(value = "4.0", inclusive = true, message = "平均GPA不能大于4.0")
    private Double averageGPA;
    
    // 额外信息
    private Boolean isIvyLeague;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Pattern(regexp = "^(http|https)://.*", message = "网站URL格式无效")
    private String website;
    
    @Pattern(regexp = "^(http|https)://.*", message = "图片URL格式无效")
    private String imageUrl;
    
    private Boolean hasScholarship;
    
    @PositiveOrZero(message = "学费必须为非负数")
    private Double tuitionFee;
    
    @Column(columnDefinition = "TEXT")
    private String admissionRequirements;
    
    @ElementCollection
    @CollectionTable(name = "school_top_programs", joinColumns = @JoinColumn(name = "school_id"))
    @Column(name = "program")
    private List<String> topPrograms = new ArrayList<>();
    
    // 文书要求相关字段
    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EssayRequirement> essayRequirements = new ArrayList<>();
    
    // 显式添加getter和setter方法
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public Double getAcceptanceRate() {
        return acceptanceRate;
    }

    public void setAcceptanceRate(Double acceptanceRate) {
        this.acceptanceRate = acceptanceRate;
    }

    public Integer getAverageGREVerbal() {
        return averageGREVerbal;
    }

    public void setAverageGREVerbal(Integer averageGREVerbal) {
        this.averageGREVerbal = averageGREVerbal;
    }

    public Integer getAverageGREQuant() {
        return averageGREQuant;
    }

    public void setAverageGREQuant(Integer averageGREQuant) {
        this.averageGREQuant = averageGREQuant;
    }

    public Double getAverageGREAW() {
        return averageGREAW;
    }

    public void setAverageGREAW(Double averageGREAW) {
        this.averageGREAW = averageGREAW;
    }

    public Integer getAverageGMAT() {
        return averageGMAT;
    }

    public void setAverageGMAT(Integer averageGMAT) {
        this.averageGMAT = averageGMAT;
    }

    public Double getAverageGPA() {
        return averageGPA;
    }

    public void setAverageGPA(Double averageGPA) {
        this.averageGPA = averageGPA;
    }

    public Boolean getIsIvyLeague() {
        return isIvyLeague;
    }

    public void setIsIvyLeague(Boolean isIvyLeague) {
        this.isIvyLeague = isIvyLeague;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public Boolean getHasScholarship() {
        return hasScholarship;
    }

    public void setHasScholarship(Boolean hasScholarship) {
        this.hasScholarship = hasScholarship;
    }

    public Double getTuitionFee() {
        return tuitionFee;
    }

    public void setTuitionFee(Double tuitionFee) {
        this.tuitionFee = tuitionFee;
    }

    public String getAdmissionRequirements() {
        return admissionRequirements;
    }

    public void setAdmissionRequirements(String admissionRequirements) {
        this.admissionRequirements = admissionRequirements;
    }

    public List<String> getTopPrograms() {
        return topPrograms;
    }

    public void setTopPrograms(List<String> topPrograms) {
        this.topPrograms = topPrograms;
    }

    public List<EssayRequirement> getEssayRequirements() {
        return essayRequirements;
    }

    public void setEssayRequirements(List<EssayRequirement> essayRequirements) {
        this.essayRequirements = essayRequirements;
    }
} 