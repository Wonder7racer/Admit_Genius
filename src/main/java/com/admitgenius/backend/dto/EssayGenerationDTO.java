package com.admitgenius.backend.dto;

import lombok.Data;
import java.util.List;

/**
 * 文书生成DTO
 * 参照文档5.4.1中定义的文书生成请求体结构
 */
@Data
public class EssayGenerationDTO {
    // 基本信息
    private Long userId;             // 用户ID

    // 申请信息
    private String schoolName;        // 学校名称
    private String programName;      // 项目名称
    private String programType;      // 项目类型 (硕士/博士)
    private String applicationType;  // 申请类型 (秋季/春季)
    private String applicationYear;  // 申请年份
    private String applicationRound; // 申请轮次 (第一轮/第二轮)

    // 文书要求
    private String essayTitle;           // 文书标题
    private String essayPrompt;          // 文书提示语

    private String essayType;            // 文书类型

    private Integer minWordCount;        // 最小字数
    private Integer maxWordCount;        // 最大字数

    //个人信息
    // 学术背景
    private String major;            // 专业
    private String degree;           // 学位 (本科/硕士/博士)
    private String currentSchool;    // 当前/毕业院校（本科院校）
    private Double gpa;              // GPA
    private String gradeScale;       // GPA满分制 (如4.0, 5.0)
    
    // 语言成绩
    private Integer toeflScore;      // 托福总分
    private Integer toeflReading;    // 托福阅读
    private Integer toeflListening;  // 托福听力
    private Integer toeflSpeaking;   // 托福口语
    private Integer toeflWriting;    // 托福写作
    
    private Double ieltsScore;       // 雅思总分
    private Double ieltsReading;     // 雅思阅读
    private Double ieltsListening;   // 雅思听力
    private Double ieltsSpeaking;    // 雅思口语
    private Double ieltsWriting;     // 雅思写作
    
    private Integer greTotal;        // GRE总分
    private Integer greVerbal;       // GRE语文
    private Integer greQuantitative; // GRE数学
    private Double greAnalytical;    // GRE分析性写作
    
    private Integer gmatTotal;       // GMAT总分
    private Integer gmatVerbal;      // GMAT语文
    private Integer gmatQuantitative; // GMAT数学
    private Integer gmatIntegrated;   // GMAT综合推理
    private Double gmatAnalytical;    // GMAT分析性写作
    
    // 经历背景
    private List<String> researchExperience;    // 研究经历
    private List<String> workExperience;        // 工作经历
    private List<String> projectExperience;     // 项目经历
    private List<String> volunteerExperience;   // 志愿者经历
    private List<String> leadershipExperience;  // 领导经历
    private List<String> awards;                // 获奖情况
    private List<String> publications;          // 发表的论文/著作
    
    // 个人特质
    private List<String> strengths;            // 个人优势
    private List<String> weaknesses;           // 个人劣势
    private List<String> careerGoals;          // 职业目标
    private String personalStatement;          // 个人陈述
    private String motivationForApplication;   // 申请动机
    
    // 特定学校信息
    private String whyThisSchool;              // 为什么选择该学校
    private String fitWithProgram;             // 与项目的匹配度
    private List<String> specificProfessorsOfInterest; // 感兴趣的教授
    private List<String> specificCoursesOfInterest;    // 感兴趣的课程
} 