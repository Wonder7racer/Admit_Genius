package com.admitgenius.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式无效")
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @NotBlank(message = "密码不能为空")
    @JsonIgnore
    @Column(nullable = false)
    private String password;
    
    @Size(max = 100, message = "姓名长度不能超过100个字符")
    @Column(name = "full_name")
    @JsonProperty("fullName")
    private String fullName;
    
    @Size(max = 20, message = "电话号码长度不能超过20个字符")
    @Column(length = 20)
    private String phone;
    
    @Column(name = "profile_picture")
    @JsonProperty("profilePicture")
    private String profilePicture;
    
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    
    @NotNull(message = "用户状态不能为空")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private UserStatus status = UserStatus.ACTIVE;
    
    @NotNull(message = "用户角色不能为空")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private com.admitgenius.backend.model.UserRole role = com.admitgenius.backend.model.UserRole.USER;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // 学生用户特有属性
    @Column(name = "current_school", length = 100)
    @JsonProperty("undergraduateSchool")
    private String currentSchool;
    @Column(name = "target_major", length = 100)
    private String targetMajor;
    private Double gpa;
    @Column(name = "sat_score")
    private Integer satScore;
    @Column(name = "act_score")
    private Integer actScore;
    @Column(name = "toefl_score")
    private Integer toeflScore;
    @Column(name = "ielts_score")
    private Double ieltsScore;
    @Column(name = "gre_verbal")
    private Integer greVerbal;
    @Column(name = "gre_quant")
    private Integer greQuant;
    @Column(name = "gre_writing")
    private Double greWriting;
    @Column(name = "gre_combined")
    @JsonProperty("greScore")
    private Integer greCombined;
    @Column(name = "gmat_total")
    @JsonProperty("gmatScore")
    private Integer gmatTotal;
    @Column(name = "graduation_year")
    private Integer graduationYear;
    
    // 专家用户特有属性
    @Column(length = 100)
    private String institution;
    @Column(name = "expertise_area", length = 100)
    private String expertiseArea;
    @Column(length = 100)
    private String title;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String bio;
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;
    @Column(name = "rating_avg")
    private Double ratingAvg;
    @Column(name = "review_count")
    private Integer reviewCount;
    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;
    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;
    
    // 管理员特有属性
    @Column(name = "admin_level")
    private Integer adminLevel;
    @Column(length = 100)
    private String department;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String permissions;
    
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Essay> essays = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ForumPost> forumPosts = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
} 