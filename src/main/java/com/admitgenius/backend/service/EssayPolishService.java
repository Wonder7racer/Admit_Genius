package com.admitgenius.backend.service;
import com.admitgenius.backend.config.OpenAIConfig;
import com.admitgenius.backend.dto.EssayDTO;
import com.admitgenius.backend.dto.EssayPolishDTO;
import com.admitgenius.backend.model.Essay;
import com.admitgenius.backend.repository.EssayRepository;
import com.admitgenius.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EssayPolishService {

    @Autowired
    private EssayRepository essayRepository;
    
    @Autowired
    private UserRepository userRepository;

    
    @Autowired
    private OpenAIConfig openaiConfig; // 注入配置类

    private String openaiApiKey;       // 从配置类获取
    private String openaiApiUrl;       // 从配置类获取 + 端点路径
    private String openaiModel = "gpt-4-turbo";

    
    /**
     * 对文书进行润色
     * @param polishDTO 润色请求DTO
     * @return 润色后的文书DTO
     */
    public EssayDTO polishEssay(EssayPolishDTO polishDTO) {
        // 参数校验
        if (polishDTO.getEssayId() == null) {
            throw new IllegalArgumentException("必须提供有效的文书ID");
        }

        // 从数据库获取现有文书
        Essay essay = essayRepository.findById(polishDTO.getEssayId())
                .orElseThrow(() -> new RuntimeException("找不到ID为" + polishDTO.getEssayId() + "的文书"));
        
        // 获取原始内容
        String originalContent = essay.getContent();
        
        // 构建润色提示（传入原始内容）
        String prompt = buildPolishPrompt(polishDTO, originalContent);
        
        // 调用OpenAI API进行润色
        String polishedContent = callOpenAIApi(prompt);
        
        // 更新文书内容和时间戳
        essay.setContent(polishedContent);
        essay.setUpdatedAt(LocalDateTime.now());
        
        // 保存更新后的文书
        Essay savedEssay = essayRepository.save(essay);
        
        return convertToDTO(savedEssay);
    }
    
    /**
     * 构建润色提示
     * @param polishDTO 润色请求
     * @param originalContent 原始文书内容
     * @return 构建的提示
     */
    private String buildPolishPrompt(EssayPolishDTO polishDTO, String originalContent) {
        StringBuilder prompt = new StringBuilder();
        
        // 基本指令
        prompt.append("你是一位专业的留学申请文书顾问，擅长优化和润色申请文书。");
        prompt.append("请对以下申请文书进行润色和优化，使其更具说服力和吸引力。\n\n");

        // 原始文书内容
        prompt.append("\n## 原始文书内容\n");
        prompt.append(originalContent).append("\n");
        
        // 润色选项
        prompt.append("## 润色要求\n");
        
        // 添加润色选项
        if (polishDTO.getImproveStructure()) {
            prompt.append("- 改进文章结构，确保逻辑清晰，段落组织合理\n");
        }
        
        if (polishDTO.getEnhanceLanguage()) {
            prompt.append("- 提升语言表达，使用更精准、生动的词汇和表达方式\n");
        }
        
        if (polishDTO.getCheckGrammar()) {
            prompt.append("- 纠正语法错误，确保语言规范\n");
        }
        
        if (polishDTO.getReduceClichés()) {
            prompt.append("- 减少陈词滥调，避免常见的申请文书套话\n");
        }
        
        if (polishDTO.getAddExamples()) {
            prompt.append("- 在适当位置补充具体例子，增强论述的说服力\n");
        }

        if (polishDTO.getTonePreference() != null && !polishDTO.getTonePreference().isEmpty()) {
            prompt.append("- 语言风格: ").append(polishDTO.getTonePreference()).append("\n");
        }

        
        // 内容指导
        if (polishDTO.getFocusPoints() != null && !polishDTO.getFocusPoints().isEmpty()) {
            prompt.append("\n## 重点关注\n");
            for (String point : polishDTO.getFocusPoints()) {
                prompt.append("- ").append(point).append("\n");
            }
        }
        
        if (polishDTO.getAvoidPoints() != null && !polishDTO.getAvoidPoints().isEmpty()) {
            prompt.append("\n## 避免内容\n");
            for (String point : polishDTO.getAvoidPoints()) {
                prompt.append("- ").append(point).append("\n");
            }
        }
             
        if (polishDTO.getTargetWordCount() != null) {
            prompt.append("- 目标字数: 约").append(polishDTO.getTargetWordCount()).append("字\n");
        }
        

        // 最终指令
        prompt.append("\n## 润色指南\n");
        prompt.append("1. 保留原文的核心信息和个人经历\n");
        prompt.append("2. 保持作者的个人声音和风格\n");
        prompt.append("3. 增强文章的连贯性和故事性\n");
        prompt.append("4. 强化文章的独特性和个人特色\n");
        prompt.append("5. 确保内容真实可信，避免过度夸张\n");
        prompt.append("6. 直接输出润色后的完整文书，不要包含解释或修改说明\n");
        
        return prompt.toString();
    }
    
    /**
     * 调用OpenAI API
     */
    private String callOpenAIApi(String prompt) {
        try {
            openaiApiKey = openaiConfig.getApi().getKey();
            openaiApiUrl = openaiConfig.getApi().getProxyUrl() + "/chat/completions";
            
            if (openaiApiKey == null || openaiApiKey.trim().isEmpty()) {
                throw new RuntimeException("OpenAI API密钥未配置");
            }

            RestTemplate restTemplate = new RestTemplate();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openaiApiKey);
            
            Map<String, Object> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", openaiModel);
            requestBody.put("messages", new Object[]{message});
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 4000);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(openaiApiUrl, entity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && responseBody.containsKey("choices")) {
                    List<Object> choicesList = (List<Object>) responseBody.get("choices");
                    if (choicesList != null && !choicesList.isEmpty()) {
                        Map<String, Object> choice = (Map<String, Object>) choicesList.get(0);
                        Map<String, Object> messageResponse = (Map<String, Object>) choice.get("message");
                        return (String) messageResponse.get("content");
                    }
                }
                throw new RuntimeException("OpenAI API响应格式异常");
            }
            
            throw new RuntimeException("调用OpenAI API失败: " + response.getStatusCode());
            
        } catch (Exception e) {
            System.err.println("调用OpenAI API出错: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("调用OpenAI API出错: " + e.getMessage(), e);
        }
    }
    
    /**
     * 计算文本的词数
     */
    private Integer countWords(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        
        // 英文按空格分词
        String[] words = text.split("\\s+");
        return words.length;
    }
    
    /**
     * 转换为DTO
     */
    private EssayDTO convertToDTO(Essay essay) {
        EssayDTO dto = new EssayDTO();
        dto.setId(essay.getId());
        dto.setUserId(essay.getUser().getId());
        dto.setTitle(essay.getTitle());
        dto.setContent(essay.getContent());
        //dto.setVersion(essay.getVersion());
        dto.setEssayType(essay.getEssayType().name());
        dto.setCreatedAt(essay.getCreatedAt());
        dto.setUpdatedAt(essay.getUpdatedAt());
        dto.setGeneratedBy(essay.getGeneratedBy().name());
        return dto;
    }


    
    /**
     * 获取文书润色建议
     * @param essayId 文书ID
     * @return 润色建议的映射
     */
    public Map<String, Object> getPolishSuggestions(Long essayId) {
        Essay essay = essayRepository.findById(essayId)
                .orElseThrow(() -> new RuntimeException("文书不存在"));
        
        Map<String, Object> suggestions = new HashMap<>();
        suggestions.put("grammarIssues", analyzeGrammar(essay.getContent()));
        suggestions.put("structureAnalysis", analyzeStructure(essay.getContent()));
        suggestions.put("contentSuggestions", generateContentSuggestions(essay.getContent()));
        suggestions.put("wordCount", countWords(essay.getContent()));
        
        return suggestions;
    }
    
    private List<String> analyzeGrammar(String content) {
        // 这里应调用AI或语法分析工具进行分析
        // 暂时返回一些示例建议
        return List.of(
            "注意句子长度，避免过长句子",
            "检查标点符号使用是否正确",
            "确保时态一致性"
        );
    }
    
    private Map<String, Object> analyzeStructure(String content) {
        Map<String, Object> analysis = new HashMap<>();
        analysis.put("paragraphCount", content.split("\n\n").length);
        analysis.put("readabilityScore", calculateReadabilityScore(content));
        analysis.put("suggestions", List.of(
            "确保每段有明确的主题句",
            "增强段落之间的过渡",
            "考虑添加更有力的结论"
        ));
        return analysis;
    }
    
    private double calculateReadabilityScore(String content) {
        // 简化的可读性评分计算
        return 85.0; // 示例分数
    }
    
    private List<Map<String, Object>> generateContentSuggestions(String content) {
        // 生成内容改进建议
        List<Map<String, Object>> suggestions = new ArrayList<>();
        
        Map<String, Object> suggestion1 = new HashMap<>();
        suggestion1.put("type", "enhancement");
        suggestion1.put("message", "考虑添加更具体的例子来支持你的观点");
        suggestions.add(suggestion1);
        
        Map<String, Object> suggestion2 = new HashMap<>();
        suggestion2.put("type", "reduction");
        suggestion2.put("message", "避免过多使用陈词滥调的表达方式");
        suggestions.add(suggestion2);
        
        return suggestions;
    }
} 