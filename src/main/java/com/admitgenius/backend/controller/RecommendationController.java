package com.admitgenius.backend.controller;

import com.admitgenius.backend.dto.RecommendationRequestDTO;
import com.admitgenius.backend.dto.RecommendationResponseDTO;
import com.admitgenius.backend.dto.SchoolDTO;
import com.admitgenius.backend.service.RecommendationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommendations")
@Validated
public class RecommendationController {
    @Autowired
    private RecommendationService recommendationService;
    
    /**
     * 生成推荐
     * 文档 4.6.1: POST /api/recommendations/generate
     * 
     * 根据用户提供的信息生成学校或项目推荐
     * @param request 包含用户背景和偏好的请求DTO
     * @return 推荐结果
     */
    @PostMapping("/generate")
    public ResponseEntity<RecommendationResponseDTO> generateRecommendation(@Valid @RequestBody RecommendationRequestDTO request) {
        RecommendationResponseDTO recommendation = recommendationService.generateRecommendation(request);
        return ResponseEntity.ok(recommendation);
    }
    
    /**
     * 获取特定用户的推荐
     * 文档 4.6.2: GET /api/recommendations/user/{userId}
     * 
     * 获取特定用户的历史推荐结果
     * @param userId 用户ID
     * @return 用户的推荐列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RecommendationResponseDTO>> getUserRecommendations(@PathVariable Long userId) {
        List<RecommendationResponseDTO> recommendations = recommendationService.getUserRecommendations(userId);
        return ResponseEntity.ok(recommendations);
    }
    
    /**
     * 提交对推荐项目的反馈
     * 文档 4.6.3: POST /api/recommendations/feedback/{itemId}
     * 
     * 提交用户对特定推荐项的反馈
     * @param itemId 推荐项ID
     * @param feedback 包含反馈内容和是否已申请的信息
     * @return 成功响应
     */
    @PostMapping("/feedback/{itemId}")
    public ResponseEntity<?> provideFeedback(
            @PathVariable Long itemId,
            @RequestBody Map<String, Object> feedback) {
        
        String feedbackText = (String) feedback.get("feedback");
        boolean applied = Boolean.TRUE.equals(feedback.get("applied"));
        
        recommendationService.provideFeedback(itemId, feedbackText, applied);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 获取用户的简化推荐
     * 文档 4.6.4: GET /api/recommendations/simple/{userId}
     * 
     * 获取针对用户的简化学校推荐列表
     * @param userId 用户ID
     * @return 简化的学校推荐列表
     */
    @GetMapping("/simple/{userId}")
    public ResponseEntity<List<SchoolDTO>> getSimpleRecommendations(@PathVariable Long userId) {
        List<SchoolDTO> recommendations = recommendationService.recommendSchools(userId);
        return ResponseEntity.ok(recommendations);
    }
    
    /**
     * 获取所有学校的列表
     * 文档 4.6.5: GET /api/recommendations/schools
     * 
     * 获取系统中所有学校的列表
     * @return 学校列表
     */
    @GetMapping("/schools")
    public ResponseEntity<List<SchoolDTO>> getAllSchools() {
        List<SchoolDTO> schools = recommendationService.getAllSchools();
        return ResponseEntity.ok(schools);
    }
    
    /**
     * 根据ID获取特定学校的详情
     * 文档 4.6.6: GET /api/recommendations/schools/{id}
     * 
     * 获取特定学校的详细信息
     * @param id 学校ID
     * @return 学校详情
     */
    @GetMapping("/schools/{id}")
    public ResponseEntity<SchoolDTO> getSchoolById(@PathVariable Long id) {
        SchoolDTO school = recommendationService.getSchoolById(id);
        return ResponseEntity.ok(school);
    }
} 