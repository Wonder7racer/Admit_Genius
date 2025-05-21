package com.admitgenius.backend.dto;

import lombok.Data;
import java.util.List;

/**
 * 学校数据传输对象
 * 参照文档5.8.1中定义的学校信息字段
 */
@Data
public class SchoolDTO {
    private Long id;
    private String name;
    private String location;
    private Integer ranking;
    private Double acceptanceRate;
    
    // 平均分数
    private Integer averageGREVerbal;
    private Integer averageGREQuant;
    private Double averageGREAW;
    private Integer averageGMAT;
    private Double averageGPA;
    
    // 基本信息
    private Boolean isIvyLeague;
    private String description;
    private String website;
    private String imageUrl;
    private Boolean hasScholarship;
    private Double tuitionFee;
    
    // 额外信息
    private String admissionRequirements;
    private List<String> topPrograms;
    
    // 显式添加getter和setter方法，以防Lombok注解无法正常工作
    
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
} 