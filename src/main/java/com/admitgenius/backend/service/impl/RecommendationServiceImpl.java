package com.admitgenius.backend.service.impl;

import com.admitgenius.backend.dto.RecommendationRequestDTO;
import com.admitgenius.backend.dto.RecommendationResponseDTO;
import com.admitgenius.backend.dto.RecommendationResponseDTO.RecommendationItemDTO;
import com.admitgenius.backend.dto.SchoolDTO;
import com.admitgenius.backend.model.*;
import com.admitgenius.backend.repository.*;
import com.admitgenius.backend.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SchoolRepository schoolRepository;
    
    @Autowired
    private SchoolProgramRepository schoolProgramRepository;
    
    
    
    @Autowired
    private RecommendationRepository recommendationRepository;
    
    @Autowired
    private RecommendationItemRepository recommendationItemRepository;
    
    @Override
    @Transactional
    public RecommendationResponseDTO generateRecommendation(RecommendationRequestDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
       
        // 2. 创建推荐记录
        Recommendation recommendation = new Recommendation();
        recommendation.setStudent(user);
        recommendation.setCreatedAt(LocalDateTime.now());
        recommendation.setInputSummary(generateInputSummary(request));
        recommendation.setRecommendationType(
                Recommendation.RecommendationType.valueOf(request.getRecommendationType()));
        
        recommendation = recommendationRepository.save(recommendation);
        
        // 3. 获取所有学校
        List<School> allSchools = schoolRepository.findAll();
        
        // 4. 计算匹配分数并排序
        List<SchoolMatchResult> matchResults = calculateMatchScores(allSchools, request);
        
        // 5. 获取排名靠前的N所学校
        List<SchoolMatchResult> topMatches = matchResults.stream()
                .sorted(Comparator.comparing(SchoolMatchResult::getMatchScore).reversed())
                .limit(request.getCount())
                .collect(Collectors.toList());
        
        // 6. 创建推荐项目
        List<RecommendationItem> items = new ArrayList<>();
        int rank = 1;
        
        for (SchoolMatchResult result : topMatches) {
            RecommendationItem item = new RecommendationItem();
            item.setRecommendation(recommendation);
            item.setSchool(result.getSchool());
            
            // 如果需要匹配项目，查找最匹配的项目
            if (request.getTargetMajor() != null && !request.getTargetMajor().isEmpty()) {
                Optional<SchoolProgram> bestProgram = findBestProgram(result.getSchool(), request.getTargetMajor());
                bestProgram.ifPresent(item::setProgram);
            }
            
            item.setMatchScore(result.getMatchScore());
            item.setRank(rank++);
            item.setMatchReason(result.getMatchReason());
            item.setIsApplied(false);
            
            items.add(recommendationItemRepository.save(item));
        }
        
        // 7. 构建响应DTO
        RecommendationResponseDTO response = new RecommendationResponseDTO();
        response.setId(recommendation.getId());
        response.setUserId(user.getId());
        response.setCreatedAt(recommendation.getCreatedAt());
        response.setExplanation(recommendation.generateExplanation());
        
        // 转换推荐项为DTO
        List<RecommendationItemDTO> itemDTOs = items.stream()
                .map(item -> convertToItemDTO(item, request))
                .collect(Collectors.toList());
        
        response.setItems(itemDTOs);
        
        // 添加统计信息
        if (itemDTOs.size() > 0) {
            Map<String, Object> stats = generateStatistics(itemDTOs);
            response.setStatistics(stats);
        }
        
        return response;
    }
    
    @Override
    public List<RecommendationResponseDTO> getUserRecommendations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
                
        List<Recommendation> recommendations = recommendationRepository.findByStudentOrderByCreatedAtDesc(user);
        
        return recommendations.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public void provideFeedback(Long itemId, String feedback, boolean applied) {
        RecommendationItem item = recommendationItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("推荐项不存在"));
        
        item.setFeedback(feedback);
        
        if (applied) {
            item.markAsApplied();
        }
        
        recommendationItemRepository.save(item);
    }
    
    // 计算学校匹配分数
    private List<SchoolMatchResult> calculateMatchScores(List<School> schools, RecommendationRequestDTO request) {
        List<SchoolMatchResult> results = new ArrayList<>();
        
        for (School school : schools) {
            float score = 0.0f;
            List<String> reasons = new ArrayList<>();
            
            // GPA匹配 (权重：30%)
            if (request.getGpa() != null && school.getAverageGPA() != null) {
                float gpaScore = calculateGPAScore(request.getGpa(), school.getAverageGPA());
                score += gpaScore * 0.3f;
                
                if (gpaScore > 0.7f) {
                    reasons.add("您的GPA与该校招生要求非常匹配");
                } else if (gpaScore > 0.5f) {
                    reasons.add("您的GPA在该校招生范围内");
                }
            }
            
            // 标准化考试匹配 (权重：30%)
            float testScore = 0.0f;
            
            // GRE分数
            if (request.getGreScore() != null) {
                float greScore = 0.0f;
                
                // GRE语文部分
                if (request.getGreVerbal() != null && school.getAverageGREVerbal() != null) {
                    float greVerbalScore = calculateGREScore(request.getGreVerbal(), school.getAverageGREVerbal());
                    greScore += greVerbalScore * 0.3f; // 语文部分占GRE分数的30%
                    
                    if (greVerbalScore > 0.8f) {
                        reasons.add("您的GRE语文分数明显高于该校平均水平");
                    } else if (greVerbalScore > 0.6f) {
                        reasons.add("您的GRE语文分数符合该校招生要求");
                    }
                }
                
                // GRE数学部分
                if (request.getGreQuantitative() != null && school.getAverageGREQuant() != null) {
                    float greQuantScore = calculateGREScore(request.getGreQuantitative(), school.getAverageGREQuant());
                    greScore += greQuantScore * 0.3f; // 数学部分占GRE分数的30%
                    
                    if (greQuantScore > 0.8f && !reasons.contains("您的GRE语文分数明显高于该校平均水平")) {
                        reasons.add("您的GRE数学分数明显高于该校平均水平");
                    } else if (greQuantScore > 0.6f && !reasons.contains("您的GRE语文分数符合该校招生要求")) {
                        reasons.add("您的GRE数学分数符合该校招生要求");
                    }
                }
                
                // GRE分析性写作部分
                if (request.getGreAnalytical() != null && school.getAverageGREAW() != null) {
                    float greAWScore = calculateGREAWScore(request.getGreAnalytical(), school.getAverageGREAW());
                    greScore += greAWScore * 0.4f; // 分析性写作占GRE分数的40%
                    
                    if (greAWScore > 0.8f && !reasons.contains("您的GRE语文分数明显高于该校平均水平") 
                            && !reasons.contains("您的GRE数学分数明显高于该校平均水平")) {
                        reasons.add("您的GRE分析性写作分数明显高于该校平均水平");
                    } else if (greAWScore > 0.6f && !reasons.contains("您的GRE语文分数符合该校招生要求") 
                            && !reasons.contains("您的GRE数学分数符合该校招生要求")) {
                        reasons.add("您的GRE分析性写作分数符合该校招生要求");
                    }
                }
                
                testScore = Math.max(testScore, greScore);
            }
            
            // GMAT分数
            if (request.getGmatScore() != null && school.getAverageGMAT() != null) {
                float gmatScore = calculateGMATScore(request.getGmatScore(), school.getAverageGMAT());
                testScore = Math.max(testScore, gmatScore);
                
                if (gmatScore > 0.8f && !reasons.contains("您的GRE语文分数明显高于该校平均水平") 
                        && !reasons.contains("您的GRE数学分数明显高于该校平均水平")
                        && !reasons.contains("您的GRE分析性写作分数明显高于该校平均水平")) {
                    reasons.add("您的GMAT分数明显高于该校平均水平");
                } else if (gmatScore > 0.6f && !reasons.contains("您的GRE语文分数符合该校招生要求") 
                        && !reasons.contains("您的GRE数学分数符合该校招生要求")
                        && !reasons.contains("您的GRE分析性写作分数符合该校招生要求")) {
                    reasons.add("您的GMAT分数符合该校招生要求");
                }
            }
            
            score += testScore * 0.3f;
            
            // 地点偏好匹配 (权重：15%)
            if (request.getLocationPreferences() != null && !request.getLocationPreferences().isEmpty()) {
                float locationScore = calculateLocationScore(school.getLocation(), request.getLocationPreferences());
                score += locationScore * 0.15f;
                
                if (locationScore > 0.9f) {
                    reasons.add("学校位置完全符合您的地区偏好");
                } else if (locationScore > 0.5f) {
                    reasons.add("学校位置与您的部分地区偏好相符");
                }
            }
            
            // 学校类型偏好匹配 (权重：15%)
            if (request.getSchoolTypePreferences() != null && !request.getSchoolTypePreferences().isEmpty()) {
                // 假设学校有一个tag或type字段
                float typeScore = 0.5f; // 默认中等匹配
                score += typeScore * 0.15f;
                
                // 如果有Ivy League偏好且学校是常春藤
                if (request.getSchoolTypePreferences().contains("Ivy League") && 
                    Boolean.TRUE.equals(school.getIsIvyLeague())) {
                    reasons.add("常春藤联盟院校符合您的学校类型偏好");
                }
            }
            
            // 专业匹配 (权重：10%)
            if (request.getTargetMajor() != null && !request.getTargetMajor().isEmpty()) {
                Optional<SchoolProgram> bestProgram = findBestProgram(school, request.getTargetMajor());
                if (bestProgram.isPresent()) {
                    score += 0.1f;
                    reasons.add("学校提供您感兴趣的 " + bestProgram.get().getName() + " 专业");
                }
            }
            
            // 创建匹配结果
            SchoolMatchResult result = new SchoolMatchResult();
            result.setSchool(school);
            result.setMatchScore(score);
            result.setMatchReason(String.join("；", reasons));
            
            results.add(result);
        }
        
        return results;
    }
    
    private float calculateGPAScore(double userGPA, double schoolGPA) {
        // 计算用户GPA与学校GPA的匹配度，返回0-1的分数
        // 基本逻辑：用户GPA >= 学校GPA时为高匹配，否则按差距减分
        if (userGPA >= schoolGPA) {
            return 1.0f;
        } else {
            float difference = (float) (schoolGPA - userGPA);
            // GPA差距每0.3减0.2分
            return Math.max(0, 1.0f - (difference / 0.3f) * 0.2f);
        }
    }
    
    /**
     * 计算GRE分数匹配度
     * @param userScore 用户GRE分数
     * @param schoolAvg 学校平均GRE分数
     * @return 匹配度分数(0-1)
     */
    private float calculateGREScore(int userScore, int schoolAvg) {
        // GRE各部分分数范围：130-170，计算匹配度
        if (userScore >= schoolAvg) {
            return 1.0f;
        } else {
            float difference = schoolAvg - userScore;
            // 差距每5分减0.2分
            return Math.max(0, 1.0f - (difference / 5.0f) * 0.2f);
        }
    }
    
    /**
     * 计算GRE分析性写作匹配度
     * @param userScore 用户GRE AW分数
     * @param schoolAvg 学校平均GRE AW分数
     * @return 匹配度分数(0-1)
     */
    private float calculateGREAWScore(double userScore, double schoolAvg) {
        // GRE AW分数范围：0.0-6.0，计算匹配度
        if (userScore >= schoolAvg) {
            return 1.0f;
        } else {
            float difference = (float) (schoolAvg - userScore);
            // 差距每0.5分减0.2分
            return Math.max(0, 1.0f - (difference / 0.5f) * 0.2f);
        }
    }
    
    /**
     * 计算GMAT分数匹配度
     * @param userScore 用户GMAT分数
     * @param schoolAvg 学校平均GMAT分数
     * @return 匹配度分数(0-1)
     */
    private float calculateGMATScore(int userScore, int schoolAvg) {
        // GMAT分数范围：200-800，计算匹配度
        if (userScore >= schoolAvg) {
            return 1.0f;
        } else {
            float difference = schoolAvg - userScore;
            // 差距每30分减0.2分
            return Math.max(0, 1.0f - (difference / 30.0f) * 0.2f);
        }
    }
    
    private float calculateLocationScore(String schoolLocation, List<String> preferredLocations) {
        if (preferredLocations.contains(schoolLocation)) {
            return 1.0f;
        }
        
        // 处理状态/地区部分匹配的情况
        for (String location : preferredLocations) {
            if (schoolLocation.contains(location) || location.contains(schoolLocation)) {
                return 0.7f;
            }
        }
        
        return 0.0f;
    }
    
    private Optional<SchoolProgram> findBestProgram(School school, String targetMajor) {
        // 假设学校与其专业之间有一对多关系
        List<SchoolProgram> programs = schoolProgramRepository.findBySchool(school);
        
        return programs.stream()
                .filter(program -> program.getName().toLowerCase().contains(targetMajor.toLowerCase()) || 
                                  program.getKeywords().stream().anyMatch(keyword -> 
                                      keyword.toLowerCase().contains(targetMajor.toLowerCase())))
                .findFirst();
    }
    
    private String generateInputSummary(RecommendationRequestDTO request) {
        StringBuilder summary = new StringBuilder();
        summary.append("GPA: ").append(request.getGpa());
        
        if (request.getGreScore() != null) {
            summary.append(", GRE: ").append(request.getGreScore());
        }
        
        if (request.getGmatScore() != null) {
            summary.append(", GMAT: ").append(request.getGmatScore());
        }
        
        if (request.getTargetMajor() != null) {
            summary.append(", 目标专业: ").append(request.getTargetMajor());
        }
        
        if (request.getLocationPreferences() != null && !request.getLocationPreferences().isEmpty()) {
            summary.append(", 地区偏好: ").append(String.join(", ", request.getLocationPreferences()));
        }
        
        return summary.toString();
    }
    
    private RecommendationItemDTO convertToItemDTO(RecommendationItem item, RecommendationRequestDTO request) {
        RecommendationItemDTO dto = new RecommendationItemDTO();
        School school = item.getSchool();
        
        dto.setId(item.getId());
        dto.setSchoolId(school.getId());
        dto.setSchoolName(school.getName());
        dto.setSchoolLocation(school.getLocation());
        dto.setSchoolLogo(school.getImageUrl());
        dto.setSchoolRanking(school.getRanking());
        
        if (item.getProgram() != null) {
            SchoolProgram program = item.getProgram();
            dto.setProgramId(program.getId());
            dto.setProgramName(program.getName());
            dto.setDepartment(program.getDepartment());
            dto.setDegreeLevel(program.getDegreeLevel().toString());
            
            if (Boolean.TRUE.equals(request.getIncludeTuitionInfo())) {
                dto.setTuitionFee(program.getTuitionFee());
                dto.setScholarshipAvailable(program.getScholarshipAvailable());
            }
        }
        
        dto.setMatchScore(item.getMatchScore());
        dto.setRank(item.getRank());
        dto.setMatchReason(item.getMatchReason());
        dto.setIsApplied(item.getIsApplied());
        dto.setFeedback(item.getFeedback());
        
        return dto;
    }
    
    private Map<String, Object> generateStatistics(List<RecommendationItemDTO> items) {
        Map<String, Object> stats = new HashMap<>();
        
        // 计算平均排名
        double avgRanking = items.stream()
                .filter(item -> item.getSchoolRanking() != null)
                .mapToInt(RecommendationItemDTO::getSchoolRanking)
                .average()
                .orElse(0);
        stats.put("avgRanking", avgRanking);
        
        // 计算平均学费
        double avgTuition = items.stream()
                .filter(item -> item.getTuitionFee() != null)
                .mapToDouble(RecommendationItemDTO::getTuitionFee)
                .average()
                .orElse(0);
        stats.put("avgTuition", avgTuition);
        
        // 地区分布
        Map<String, Integer> locationDistribution = new HashMap<>();
        items.forEach(item -> {
            locationDistribution.merge(item.getSchoolLocation(), 1, Integer::sum);
        });
        stats.put("locationDistribution", locationDistribution);
        
        return stats;
    }
    
    private RecommendationResponseDTO convertToResponseDTO(Recommendation recommendation) {
        // 将Recommendation实体转换为ResponseDTO
        RecommendationResponseDTO dto = new RecommendationResponseDTO();
        dto.setId(recommendation.getId());
        dto.setUserId(recommendation.getStudent().getId());
        dto.setCreatedAt(recommendation.getCreatedAt());
        dto.setExplanation(recommendation.generateExplanation());
        
        // 获取并转换所有推荐项
        List<RecommendationItemDTO> itemDTOs = recommendationItemRepository.findByRecommendation(recommendation)
                .stream()
                .map(item -> {
                    RecommendationRequestDTO dummyRequest = new RecommendationRequestDTO();
                    dummyRequest.setIncludeAdmissionStatistics(false);
                    dummyRequest.setIncludeTuitionInfo(true);
                    return convertToItemDTO(item, dummyRequest);
                })
                .collect(Collectors.toList());
        
        dto.setItems(itemDTOs);
        
        if (!itemDTOs.isEmpty()) {
            Map<String, Object> stats = generateStatistics(itemDTOs);
            dto.setStatistics(stats);
        }
        
        return dto;
    }
    
    // 辅助内部类，用于存储匹配结果
    private static class SchoolMatchResult {
        private School school;
        private float matchScore;
        private String matchReason;
        
        public School getSchool() {
            return school;
        }
        
        public void setSchool(School school) {
            this.school = school;
        }
        
        public float getMatchScore() {
            return matchScore;
        }
        
        public void setMatchScore(float matchScore) {
            this.matchScore = matchScore;
        }
        
        public String getMatchReason() {
            return matchReason;
        }
        
        public void setMatchReason(String matchReason) {
            this.matchReason = matchReason;
        }
    }
    
    @Override
    public List<SchoolDTO> recommendSchools(Long userId) {
        // 获取用户信息
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 获取所有学校
        List<School> allSchools = schoolRepository.findAll();
        
        // 筛选匹配的学校
        List<School> matchingSchools = allSchools.stream()
                .filter(school -> isGoodMatch(user, school))
                .collect(Collectors.toList());
        
        // 如果匹配的学校不足10所，添加一些排名较好的学校作为补充
        if (matchingSchools.size() < 10) {
            int neededMore = 10 - matchingSchools.size();
            
            // 获取已经匹配学校的ID列表，用于排除
            List<Long> matchedIds = matchingSchools.stream()
                    .map(School::getId)
                    .collect(Collectors.toList());
            
            // 获取未匹配但排名靠前的学校
            List<School> topSchools = allSchools.stream()
                    .filter(school -> !matchedIds.contains(school.getId()))
                    .sorted(Comparator.comparing(School::getRanking, 
                            Comparator.nullsLast(Comparator.naturalOrder())))
                    .limit(neededMore)
                    .collect(Collectors.toList());
            
            matchingSchools.addAll(topSchools);
        }
        
        // 按排名排序结果
        matchingSchools.sort(Comparator.comparing(School::getRanking, 
                Comparator.nullsLast(Comparator.naturalOrder())));
        
        // 转换为DTO并返回
        return matchingSchools.stream()
                .map(this::convertToSchoolDTO)
                .collect(Collectors.toList());
    }
    
    private boolean isGoodMatch(User user, School school) {
        // 检查必要的用户信息是否存在
        if (user.getGpa() == null) {
            return false;
        }

        // 检查学校的必要信息是否存在
        if (school.getAverageGPA() == null) {
            return false;
        }

        // 计算用户与学校的匹配度
        // GPA匹配
        boolean gpaMatch = false;
        double userGpa = user.getGpa();
        double schoolGpa = school.getAverageGPA();
        if (userGpa >= schoolGpa) {
            gpaMatch = true;
        }

        // GRE分数匹配
        boolean greMatch = false;
        // 使用组合的GRE分数（如果存在）
        Integer greCombined = user.getGreCombined();
        
        // 从学校获取GRE阈值（如果存在）
        Integer schoolGreVerbal = school.getAverageGREVerbal();
        Integer schoolGreQuant = school.getAverageGREQuant();
        
        // 计算学校的GRE组合分数阈值
        Integer schoolGreCombined = null;
        if (schoolGreVerbal != null && schoolGreQuant != null) {
            schoolGreCombined = schoolGreVerbal + schoolGreQuant;
        }
        
        // 如果没有学校GRE组合分数，使用默认阈值
        int greThreshold = (schoolGreCombined != null) ? schoolGreCombined : 310;
        
        // 检查用户的组合GRE分数
        if (greCombined != null) {
            if (greCombined >= greThreshold) {
                greMatch = true;
            }
        } else {
            // 如果没有组合分数，尝试使用单独的verbal和quant分数计算
            Integer greVerbal = user.getGreVerbal();
            Integer greQuant = user.getGreQuant();
            
            // 如果有单独分数，比较各自部分
            if (greVerbal != null && greQuant != null) {
                // 检查单独的部分分数是否达到学校要求
                boolean verbalMatch = schoolGreVerbal == null || greVerbal >= schoolGreVerbal;
                boolean quantMatch = schoolGreQuant == null || greQuant >= schoolGreQuant;
                
                // 如果学校没有单独部分要求，检查组合分数
                if (!verbalMatch || !quantMatch) {
                    int combinedScore = greVerbal + greQuant;
                    if (combinedScore >= greThreshold) {
                        greMatch = true;
                    }
                } else {
                    greMatch = true;
                }
            }
        }

        // 如果GPA匹配且GRE分数匹配，则返回true
        return gpaMatch && greMatch;
    }
    
    private SchoolDTO convertToSchoolDTO(School school) {
        SchoolDTO dto = new SchoolDTO();
        dto.setId(school.getId());
        dto.setName(school.getName());
        dto.setLocation(school.getLocation());
        dto.setRanking(school.getRanking());
        dto.setAcceptanceRate(school.getAcceptanceRate());
        
        // 设置GRE和其他考试分数字段
        dto.setAverageGREVerbal(school.getAverageGREVerbal());
        dto.setAverageGREQuant(school.getAverageGREQuant());
        dto.setAverageGREAW(school.getAverageGREAW());
        dto.setAverageGMAT(school.getAverageGMAT());
        dto.setAverageGPA(school.getAverageGPA());
        
        // 设置其他字段
        dto.setDescription(school.getDescription());
        dto.setWebsite(school.getWebsite());
        dto.setImageUrl(school.getImageUrl());
        dto.setHasScholarship(school.getHasScholarship());
        dto.setTuitionFee(school.getTuitionFee());
        dto.setAdmissionRequirements(school.getAdmissionRequirements());
        dto.setTopPrograms(school.getTopPrograms());
        
        return dto;
    }
    
    @Override
    public List<SchoolDTO> getAllSchools() {
        return schoolRepository.findAll().stream()
                .map(this::convertToSchoolDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public SchoolDTO getSchoolById(Long id) {
        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("学校不存在"));
        return convertToSchoolDTO(school);
    }
} 