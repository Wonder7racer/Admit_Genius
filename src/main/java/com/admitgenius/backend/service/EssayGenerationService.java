package com.admitgenius.backend.service;
import com.admitgenius.backend.config.OpenAIConfig;
import com.admitgenius.backend.dto.EssayDTO;
import com.admitgenius.backend.dto.EssayGenerationDTO;
import com.admitgenius.backend.model.*;
import com.admitgenius.backend.repository.EssayRepository;
import com.admitgenius.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Objects;

@Service
public class EssayGenerationService {

    @Autowired
    private UserRepository userRepository;
    
    
    @Autowired
    private EssayRepository essayRepository;
    
    
    @Autowired
    private OpenAIConfig openaiConfig; // 注入配置类

    private String openaiApiKey;       // 从配置类获取
    private String openaiApiUrl;       // 从配置类获取 + 端点路径
    private String openaiModel = "gpt-4-turbo";
    
    /**
     * 根据用户提交的信息自动生成文书
     */
    public EssayDTO generateEssay(EssayGenerationDTO generationDTO) {
        // 1. 获取必要的实体数据
        User user = userRepository.findById(generationDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
                

        // 2. 构建AI提示语
        String prompt = buildPrompt(generationDTO);
        
        // 3. 调用OpenAI API生成文书
        String generatedContent = callOpenAIApi(prompt);
        
        // 4. 保存生成的文书
        Essay essay = new Essay();
        essay.setUser(user);
        essay.setTitle(generationDTO.getEssayTitle());
        essay.setContent(generatedContent);
        //essay.setVersion(0); 
        essay.setEssayType(Essay.EssayType.valueOf(generationDTO.getEssayType()));
        essay.setCreatedAt(LocalDateTime.now());
        essay.setUpdatedAt(LocalDateTime.now());
        essay.setGeneratedBy(Essay.GenerationSource.AI_MODEL);
        
        Essay savedEssay = essayRepository.save(essay);
        
        // 5. 转换为DTO并返回
        return convertToDTO(savedEssay);
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
     * 构建AI提示语
     */
    // private String buildPrompt(EssayGenerationDTO dto, EssayRequirement requirement, School school) {
    //     StringBuilder prompt = new StringBuilder();
        
    //     // 基本指令
    //     prompt.append("你是一位专业的留学申请文书顾问，擅长为申请者撰写个性化、有说服力的申请文书。");
    //     prompt.append("请根据以下信息为申请者撰写一篇针对特定学校和项目的申请文书。\n\n");

        
    //     // 文书要求信息
    //     prompt.append("## 文书要求\n");
    //     prompt.append("文书类型: ").append(requirement.getEssayType()).append("\n");
    //     prompt.append("文书标题: ").append(requirement.getTitle()).append("\n");
    //     prompt.append("文书提示语: ").append(requirement.getPrompt()).append("\n");
    //     prompt.append("字数要求: ").append(requirement.getMinWordCount()).append("-").append(requirement.getMaxWordCount()).append(" 字\n");
    //     if (requirement.getAdditionalInstructions() != null) {
    //         prompt.append("附加说明: ").append(requirement.getAdditionalInstructions()).append("\n");
    //     }
    //     prompt.append("\n");
        
    //     // 申请者基本背景
    //     prompt.append("## 申请者背景\n");
    //     prompt.append("专业: ").append(dto.getMajor()).append("\n");
    //     prompt.append("学位: ").append(dto.getDegree()).append("\n");
    //     prompt.append("当前/毕业院校: ").append(dto.getCurrentSchool()).append("\n");
    //     prompt.append("GPA: ").append(dto.getGpa()).append("/").append(dto.getGradeScale()).append("\n");
        
    //     // 语言成绩
    //     prompt.append("\n## 语言成绩\n");
    //     if (dto.getToeflScore() != null) {
    //         prompt.append("托福: 总分 ").append(dto.getToeflScore());
    //         if (dto.getToeflReading() != null) {
    //             prompt.append(" (阅读:").append(dto.getToeflReading())
    //                   .append(", 听力:").append(dto.getToeflListening())
    //                   .append(", 口语:").append(dto.getToeflSpeaking())
    //                   .append(", 写作:").append(dto.getToeflWriting()).append(")");
    //         }
    //         prompt.append("\n");
    //     }
        
    //     if (dto.getIeltsScore() != null) {
    //         prompt.append("雅思: 总分 ").append(dto.getIeltsScore());
    //         if (dto.getIeltsReading() != null) {
    //             prompt.append(" (阅读:").append(dto.getIeltsReading())
    //                   .append(", 听力:").append(dto.getIeltsListening())
    //                   .append(", 口语:").append(dto.getIeltsSpeaking())
    //                   .append(", 写作:").append(dto.getIeltsWriting()).append(")");
    //         }
    //         prompt.append("\n");
    //     }
        
    //     if (dto.getGreTotal() != null) {
    //         prompt.append("GRE: 总分 ").append(dto.getGreTotal());
    //         if (dto.getGreVerbal() != null) {
    //             prompt.append(" (语文:").append(dto.getGreVerbal())
    //                   .append(", 数学:").append(dto.getGreQuantitative())
    //                   .append(", 分析写作:").append(dto.getGreAnalytical()).append(")");
    //         }
    //         prompt.append("\n");
    //     }
        
    //     if (dto.getGmatTotal() != null) {
    //         prompt.append("GMAT: 总分 ").append(dto.getGmatTotal()).append("\n");
    //     }
        
    //     // 研究经历
    //     if (dto.getResearchExperience() != null && !dto.getResearchExperience().isEmpty()) {
    //         prompt.append("\n## 研究经历\n");
    //         for (String exp : dto.getResearchExperience()) {
    //             prompt.append("- ").append(exp).append("\n");
    //         }
    //     }
        
    //     // 工作经历
    //     if (dto.getWorkExperience() != null && !dto.getWorkExperience().isEmpty()) {
    //         prompt.append("\n## 工作经历\n");
    //         for (String exp : dto.getWorkExperience()) {
    //             prompt.append("- ").append(exp).append("\n");
    //         }
    //     }
        
    //     // 项目经历
    //     if (dto.getProjectExperience() != null && !dto.getProjectExperience().isEmpty()) {
    //         prompt.append("\n## 项目经历\n");
    //         for (String exp : dto.getProjectExperience()) {
    //             prompt.append("- ").append(exp).append("\n");
    //         }
    //     }
        
    //     // 志愿者经历
    //     if (dto.getVolunteerExperience() != null && !dto.getVolunteerExperience().isEmpty()) {
    //         prompt.append("\n## 志愿者经历\n");
    //         for (String exp : dto.getVolunteerExperience()) {
    //             prompt.append("- ").append(exp).append("\n");
    //         }
    //     }
        
    //     // 领导经历
    //     if (dto.getLeadershipExperience() != null && !dto.getLeadershipExperience().isEmpty()) {
    //         prompt.append("\n## 领导经历\n");
    //         for (String exp : dto.getLeadershipExperience()) {
    //             prompt.append("- ").append(exp).append("\n");
    //         }
    //     }
        
    //     // 获奖情况
    //     if (dto.getAwards() != null && !dto.getAwards().isEmpty()) {
    //         prompt.append("\n## 获奖情况\n");
    //         for (String award : dto.getAwards()) {
    //             prompt.append("- ").append(award).append("\n");
    //         }
    //     }
        
    //     // 出版物
    //     if (dto.getPublications() != null && !dto.getPublications().isEmpty()) {
    //         prompt.append("\n## 发表论文/著作\n");
    //         for (String pub : dto.getPublications()) {
    //             prompt.append("- ").append(pub).append("\n");
    //         }
    //     }
        
    //     // 个人特质和目标
    //     prompt.append("\n## 个人特质和目标\n");
    //     if (dto.getStrengths() != null && !dto.getStrengths().isEmpty()) {
    //         prompt.append("个人优势: ");
    //         for (String strength : dto.getStrengths()) {
    //             prompt.append(strength).append(", ");
    //         }
    //         prompt.append("\n");
    //     }
        
    //     if (dto.getCareerGoals() != null && !dto.getCareerGoals().isEmpty()) {
    //         prompt.append("职业目标: ");
    //         for (String goal : dto.getCareerGoals()) {
    //             prompt.append(goal).append(", ");
    //         }
    //         prompt.append("\n");
    //     }
        
    //     if (dto.getPersonalStatement() != null) {
    //         prompt.append("个人陈述: ").append(dto.getPersonalStatement()).append("\n");
    //     }
        
    //     if (dto.getMotivationForApplication() != null) {
    //         prompt.append("申请动机: ").append(dto.getMotivationForApplication()).append("\n");
    //     }
        
    //     // 与特定学校的关联
    //     prompt.append("\n## 与学校的匹配度\n");
    //     if (dto.getWhyThisSchool() != null) {
    //         prompt.append("为什么选择该学校: ").append(dto.getWhyThisSchool()).append("\n");
    //     }
        
    //     if (dto.getFitWithProgram() != null) {
    //         prompt.append("与项目的匹配度: ").append(dto.getFitWithProgram()).append("\n");
    //     }
        
    //     if (dto.getSpecificProfessorsOfInterest() != null && !dto.getSpecificProfessorsOfInterest().isEmpty()) {
    //         prompt.append("感兴趣的教授: ");
    //         for (String professor : dto.getSpecificProfessorsOfInterest()) {
    //             prompt.append(professor).append(", ");
    //         }
    //         prompt.append("\n");
    //     }
        
    //     if (dto.getSpecificCoursesOfInterest() != null && !dto.getSpecificCoursesOfInterest().isEmpty()) {
    //         prompt.append("感兴趣的课程: ");
    //         for (String course : dto.getSpecificCoursesOfInterest()) {
    //             prompt.append(course).append(", ");
    //         }
    //         prompt.append("\n");
    //     }
        
    //     // 最终指令
    //     prompt.append("\n## 写作要求\n");
    //     prompt.append("1. 请基于以上信息，撰写一篇符合学校要求的个性化文书。\n");
    //     prompt.append("2. 文书应当呈现申请者的独特性，避免空泛内容。\n");
    //     prompt.append("3. 使用具体的例子和经历来支持你的论点。\n");
    //     prompt.append("4. 确保文章逻辑清晰，段落之间有良好的过渡。\n");
    //     prompt.append("5. 文书长度应当符合字数要求：").append(requirement.getMinWordCount()).append("-").append(requirement.getMaxWordCount()).append(" 字。\n");
    //     prompt.append("6. 语言应当专业、流畅，同时展现申请者的个性。\n");
    //     prompt.append("7. 请直接输出文书内容，不要包含标题或额外的解释。\n");
        
    //     return prompt.toString();
    // }
    

    
    private String buildPrompt(EssayGenerationDTO dto) {
        StringBuilder prompt = new StringBuilder();
        
        // 基础指令
        prompt.append("你是一位顶尖的留学申请文书顾问，需要根据申请者的完整背景信息，撰写一篇高度个性化、逻辑严谨且符合学校要求的申请文书。\n\n");
        

        // 申请与学校信息
        prompt.append("## 申请与学校信息\n");
        addField(prompt, "学校名称", dto.getSchoolName());
        addField(prompt, "目标项目", dto.getProgramName());
        addField(prompt, "项目类型", dto.getProgramType());
        addField(prompt, "申请类型", dto.getApplicationType());
        addField(prompt, "申请年份", dto.getApplicationYear());
        addField(prompt, "申请轮次", dto.getApplicationRound());
        prompt.append("\n");


        // 文书要求
        prompt.append("## 文书要求\n");
        addField(prompt, "文书标题", dto.getEssayTitle());
        addField(prompt, "文书类型", dto.getEssayType());
        addField(prompt, "官方提示", dto.getEssayPrompt());
        prompt.append("字数限制: ").append(dto.getMinWordCount()).append("-").append(dto.getMaxWordCount()).append(" 字\n\n");
        

        // 学术背景（结构化呈现）
        prompt.append("## 学术背景\n");
        addField(prompt, "本科专业", dto.getMajor());
        addField(prompt, "已获学位", dto.getDegree());
        addField(prompt, "毕业院校", dto.getCurrentSchool());
        addField(prompt, "GPA成绩", formatGPA(dto.getGpa(), dto.getGradeScale()));
        prompt.append("\n");


        // 标准化考试成绩（多考试支持）
        prompt.append("## 语言与标化成绩\n");
        // 托福成绩组
        if (dto.getToeflScore() != null) {
            prompt.append("- 托福: 总分").append(dto.getToeflScore());
            appendSubscores(prompt, 
                dto.getToeflReading(), 
                dto.getToeflListening(), 
                dto.getToeflSpeaking(), 
                dto.getToeflWriting());
        }
        // 雅思成绩组
        if (dto.getIeltsScore() != null) {
            prompt.append("- 雅思: 总分").append(dto.getIeltsScore());
            appendSubscores(prompt, 
                dto.getIeltsReading(), 
                dto.getIeltsListening(), 
                dto.getIeltsSpeaking(), 
                dto.getIeltsWriting());
        }
        // GRE/GMAT处理
        addExamDetails(prompt, "GRE", dto.getGreTotal(), dto.getGreVerbal(), dto.getGreQuantitative(), dto.getGreAnalytical());
        addExamDetails(prompt, "GMAT", dto.getGmatTotal(), dto.getGmatVerbal(), dto.getGmatQuantitative(), dto.getGmatAnalytical());
        prompt.append("\n");

        // 经历背景（动态生成模块）
        prompt.append("## 核心经历\n");
        addListSection(prompt, "研究经历", dto.getResearchExperience());
        addListSection(prompt, "工作经历", dto.getWorkExperience());
        addListSection(prompt, "项目经历", dto.getProjectExperience());
        addListSection(prompt, "领导经历", dto.getLeadershipExperience());
        addListSection(prompt, "志愿活动", dto.getVolunteerExperience());
        addListSection(prompt, "学术荣誉", dto.getAwards());
        addListSection(prompt, "发表作品", dto.getPublications());
        prompt.append("\n");

        // 个人画像
        prompt.append("## 个人特质分析\n");
        addListField(prompt, "核心优势", dto.getStrengths());
        addListField(prompt, "待改进领域", dto.getWeaknesses());
        addListField(prompt, "职业目标", dto.getCareerGoals());
        addField(prompt, "个人陈述要点", dto.getPersonalStatement());
        addField(prompt, "核心申请动机", dto.getMotivationForApplication());
        prompt.append("\n");


        // 学校匹配度
        prompt.append("## 学校匹配论证\n");
        addField(prompt, "选择该校的理由", dto.getWhyThisSchool());
        addField(prompt, "项目契合度分析", dto.getFitWithProgram());
        addListField(prompt, "目标教授", dto.getSpecificProfessorsOfInterest());
        addListField(prompt, "目标课程", dto.getSpecificCoursesOfInterest());
        prompt.append("\n");

        // 最终指令
        prompt.append("\n## 写作要求\n");
        prompt.append("1. 请基于以上信息，撰写一篇符合学校要求的个性化文书。\n");
        prompt.append("2. 文书应当呈现申请者的独特性，避免空泛内容。\n");
        prompt.append("3. 使用具体的例子和经历来支持你的论点。\n");
        prompt.append("4. 确保文章逻辑清晰，段落之间有良好的过渡。\n");
        prompt.append("5. 文书长度应当符合字数要求：").append(dto.getMinWordCount()).append("-").append(dto.getMaxWordCount()).append(" 字。\n");
        prompt.append("6. 语言应当专业、流畅，同时展现申请者的个性。\n");
        prompt.append("7. 请直接输出文书内容，不要包含标题或额外的解释。\n");

        return prompt.toString();
    }

    // ======================
    // 辅助方法集
    // ======================
    private void addField(StringBuilder prompt, String label, Object value) {
        if (value != null && !value.toString().isEmpty()) {
            prompt.append(label).append(": ").append(value).append("\n");
        }
    }

    private void addListField(StringBuilder prompt, String label, List<?> items) {
        if (items != null && !items.isEmpty()) {
            prompt.append(label).append(": ")
                .append(String.join("; ", items.stream().map(Object::toString).collect(Collectors.toList())))
                .append("\n");
        }
    }

    private void addListSection(StringBuilder prompt, String sectionTitle, List<String> items) {
        if (items != null && !items.isEmpty()) {
            prompt.append("### ").append(sectionTitle).append("\n");
            items.forEach(item -> prompt.append("- ").append(item).append("\n"));
            prompt.append("\n");
        }
    }

    private void appendSubscores(StringBuilder prompt, Object... subscores) {
        boolean hasSubscores = Arrays.stream(subscores).anyMatch(Objects::nonNull);
        if (hasSubscores) {
            prompt.append(" (")
                .append("阅读:").append(subscores[0]).append(", ")
                .append("听力:").append(subscores[1]).append(", ")
                .append("口语:").append(subscores[2]).append(", ")
                .append("写作:").append(subscores[3])
                .append(")");
        }
        prompt.append("\n");
    }

    private void addExamDetails(StringBuilder prompt, String examName, Integer total, Integer verbal, Integer quant, Double analytical) {
        if (total != null) {
            prompt.append("- ").append(examName).append(": 总分").append(total);
            if (verbal != null || quant != null) {
                prompt.append(" (")
                    .append("语文:").append(verbal != null ? verbal : "N/A").append(", ")
                    .append("数学:").append(quant != null ? quant : "N/A").append(")");
            }
            if (analytical != null) {
                prompt.append(" 分析写作:").append(analytical);
            }
            prompt.append("\n");
        }
    }

    private String formatGPA(Double gpa, String scale) {
        if (gpa == null || scale == null) return "未提供";
        return String.format("%.2f/%s（专业前%d%%）", gpa, scale, calculatePercentage(gpa, scale));
    }

    // 示例GPA百分位计算（需根据实际算法完善）
    private int calculatePercentage(Double gpa, String scale) {
        double max = Double.parseDouble(scale);
        return (int) ((gpa / max) * 100);
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
            
            // 输出调试信息
            System.out.println("调用OpenAI的API密钥: " + (openaiApiKey.length() > 10 ? 
                openaiApiKey.substring(0, 10) + "..." : "密钥过短"));
            System.out.println("调用OpenAI的URL: " + openaiApiUrl);

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
                        Map<String, Object> message1 = (Map<String, Object>) choice.get("message");
                        return (String) message1.get("content");
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
    

} 