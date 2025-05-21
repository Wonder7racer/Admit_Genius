package com.admitgenius.backend.controller;

import com.admitgenius.backend.dto.EssayDTO;
import com.admitgenius.backend.dto.EssayPolishDTO;
import com.admitgenius.backend.service.EssayPolishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/essays/polish")
public class EssayPolishController {
    
    @Autowired
    private EssayPolishService essayPolishService;
    
    /**
     * 润色文书
     * 文档 4.5.1: POST /api/essays/polish
     * 
     * 接收用户提交的文书并进行润色
     * @param polishDTO 包含原始文书和润色要求的DTO
     * @return 润色后的文书DTO
     */
    @PostMapping
    public ResponseEntity<EssayDTO> polishEssay(@RequestBody EssayPolishDTO polishDTO) {
        EssayDTO polishedEssay = essayPolishService.polishEssay(polishDTO);
        return ResponseEntity.ok(polishedEssay);
    }
    
    /**
     * 根据ID润色特定文书
     * 文档 4.5.2: POST /api/essays/polish/{essayId}
     * 
     * 对现有文书进行润色
     * @param essayId 文书ID
     * @param polishDTO 润色要求
     * @return 润色后的文书DTO
     */
    @PostMapping("/{essayId}")
    public ResponseEntity<EssayDTO> polishExistingEssay(@PathVariable Long essayId, @RequestBody EssayPolishDTO polishDTO) {
        polishDTO.setEssayId(essayId);
        EssayDTO polishedEssay = essayPolishService.polishEssay(polishDTO);
        return ResponseEntity.ok(polishedEssay);
    }
    
    /**
     * 获取特定文书的润色建议
     * 文档 4.5.3: GET /api/essays/polish/suggestions/{essayId}
     * 
     * 获取文书的润色建议（不保存结果）
     * @param essayId 文书ID
     * @return 润色建议
     */
    @GetMapping("/suggestions/{essayId}")
    public ResponseEntity<Map<String, Object>> getPolishSuggestions(@PathVariable Long essayId) {
        Map<String, Object> suggestions = essayPolishService.getPolishSuggestions(essayId);
        return ResponseEntity.ok(suggestions);
    }
} 