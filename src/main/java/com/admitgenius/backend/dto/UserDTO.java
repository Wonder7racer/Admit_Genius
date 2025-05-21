package com.admitgenius.backend.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private String fullName;
    private String profilePicture;
    private String role;
    private String undergraduateSchool;
    private Double gpa;
    private Integer greScore;
    private Integer gmatScore;
    private String targetMajor;
    private Integer toeflScore;
    private Double ieltsScore;
} 