package com.admitgenius.backend.service;

import com.admitgenius.backend.dto.RecommendationRequestDTO;
import com.admitgenius.backend.dto.RecommendationResponseDTO;
import com.admitgenius.backend.dto.SchoolDTO;

import java.util.List;

public interface RecommendationService {
    /**
     * 基于学生背景生成学校推荐
     * @param request 包含学生背景和推荐偏好的请求
     * @return 推荐响应，包含推荐学校和匹配原因
     */
    RecommendationResponseDTO generateRecommendation(RecommendationRequestDTO request);
    
    /**
     * 获取用户的所有推荐历史
     * @param userId 用户ID
     * @return 推荐响应列表
     */
    List<RecommendationResponseDTO> getUserRecommendations(Long userId);
    
    /**
     * 为特定推荐项提供反馈
     * @param itemId 推荐项ID
     * @param feedback 反馈内容
     * @param applied 是否已申请
     */
    void provideFeedback(Long itemId, String feedback, boolean applied);
    
    /**
     * 获取所有学校列表
     * @return 学校DTO列表
     */
    List<SchoolDTO> getAllSchools();
    
    /**
     * 根据ID获取学校
     * @param id 学校ID
     * @return 学校DTO
     */
    SchoolDTO getSchoolById(Long id);
      
    /**
     * 为指定用户推荐学校(简化版)
     * @param userId 用户ID
     * @return 推荐学校列表
     */
    List<SchoolDTO> recommendSchools(Long userId);
} 