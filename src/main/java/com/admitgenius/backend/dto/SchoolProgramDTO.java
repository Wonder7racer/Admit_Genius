package com.admitgenius.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class SchoolProgramDTO {
    private Long id;
    private Long schoolId; // To link back to the school
    private String schoolName; // For display purposes
    private String name;
    private String department;
    private String degreeLevel; // Using String for flexibility, will map to/from enum in service
    private Integer duration; // in years or semesters
    private Double tuitionFee;
    private Boolean scholarshipAvailable;
    private String admissionRequirements;
    private List<String> keywords;
} 