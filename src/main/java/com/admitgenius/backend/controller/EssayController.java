package com.admitgenius.backend.controller;

import com.admitgenius.backend.dto.EssayDTO;
import com.admitgenius.backend.service.EssayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/essays")
public class EssayController {
    @Autowired
    private EssayService essayService;
    
    /**
     * 创建新文书
     * 文档 4.3.1: POST /api/essays
     */
    @PostMapping
    public ResponseEntity<EssayDTO> createEssay(@RequestBody EssayDTO essayDTO) {
        return ResponseEntity.ok(essayService.createEssay(essayDTO));
    }

    /**
     * 更新文书
     * 文档 4.3.4: PUT /api/essays/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<EssayDTO> updateEssay(
            @PathVariable Long id, 
            @RequestBody EssayDTO essayDTO) {
        essayDTO.setId(id); // 绑定路径ID到DTO
        return ResponseEntity.ok(essayService.updateEssay(essayDTO));
    }

    /**
     * 删除特定文书
     * 文档 4.3.2: DELETE /api/essays/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEssay(@PathVariable Long id) {
        essayService.deleteEssay(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * 获取特定用户的所有文书
     * 文档 4.3.2: GET /api/essays/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EssayDTO>> getUserEssays(@PathVariable Long userId) {
        return ResponseEntity.ok(essayService.getUserEssays(userId));
    }
    
    /**
     * 根据ID获取特定文书
     * 文档 4.3.3: GET /api/essays/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<EssayDTO> getEssayById(@PathVariable Long id) {
        return ResponseEntity.ok(essayService.getEssayById(id));
    }
    

} 