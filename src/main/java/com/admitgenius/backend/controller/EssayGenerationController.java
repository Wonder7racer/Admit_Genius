package com.admitgenius.backend.controller;

import com.admitgenius.backend.dto.EssayDTO;
import com.admitgenius.backend.dto.EssayGenerationDTO;
import com.admitgenius.backend.service.EssayGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/essays/generation")
public class EssayGenerationController {
    
    @Autowired
    private EssayGenerationService essayGenerationService;
    
    /**
     * 生成新文书
     * 文档 4.4.1: POST /api/essays/generation
     * 
     * 接收用户提交的个人信息，调用API生成文书
     * @param generationDTO 包含用户个人信息和学校文书要求ID的DTO
     * @return 生成的文书DTO
     */
    @PostMapping
    public ResponseEntity<EssayDTO> generateEssay(@RequestBody EssayGenerationDTO generationDTO) {
        EssayDTO generatedEssay = essayGenerationService.generateEssay(generationDTO);
        return ResponseEntity.ok(generatedEssay);
    }
    

} 