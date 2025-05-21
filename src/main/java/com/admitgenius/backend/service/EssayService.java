package com.admitgenius.backend.service;

import com.admitgenius.backend.dto.EssayDTO;
import com.admitgenius.backend.model.Essay;
import com.admitgenius.backend.model.User;
import com.admitgenius.backend.repository.EssayRepository;
import com.admitgenius.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.admitgenius.backend.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EssayService {
    @Autowired
    private EssayRepository essayRepository;

    @Autowired
    private UserRepository userRepository;
    
    //文书创建
    public EssayDTO createEssay(EssayDTO essayDTO) {
        // 1. 校验userId非空并查询用户
        Long userId = essayDTO.getUserId();
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

    




        // 处理essayType
        String essayTypeStr = essayDTO.getEssayType();
        if (essayTypeStr == null) {
            throw new IllegalArgumentException("Essay type cannot be null");
        }
        Essay.EssayType essayType;
        try {
            essayType = Essay.EssayType.valueOf(essayTypeStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid essay type: " + essayTypeStr);
        }


        // 4. 创建并填充Essay实体（仅处理非空字段）
        Essay essay = new Essay();
        essay.setUser(user);
        essay.setTitle(essayDTO.getTitle());
        essay.setEssayType(Essay.EssayType.valueOf(essayDTO.getEssayType()));
        essay.setContent(essayDTO.getContent());
        //essay.setVersion(0);
        essay.setGeneratedBy(Essay.GenerationSource.STUDENT);       
        essay.setCreatedAt(LocalDateTime.now());
        essay.setUpdatedAt(LocalDateTime.now());

        
        // 5. 保存并转换为DTO
        Essay savedEssay = essayRepository.save(essay);
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




    //文书更新
    public EssayDTO updateEssay(EssayDTO essayDTO) {
        Long essayId = essayDTO.getId();
        if (essayId == null) {
            throw new IllegalArgumentException("Essay ID cannot be null");
        }
        
        Essay essay = essayRepository.findById(essayId)
            .orElseThrow(() -> new ResourceNotFoundException("Essay not found with id: " + essayId));

        
        if (essayDTO.getEssayType() != null) {
            Essay.EssayType essayType;
            try {
                essayType = Essay.EssayType.valueOf(essayDTO.getEssayType());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid essay type: " + essayDTO.getEssayType());
            }
            essay.setEssayType(essayType);
        }

        if (essayDTO.getTitle() != null) {
            essay.setTitle(essayDTO.getTitle());
        }
        if (essayDTO.getContent() != null) {
            essay.setContent(essayDTO.getContent());
        }

        //essay.setVersion(essay.getVersion() + 1);
        essay.setUpdatedAt(LocalDateTime.now());
        essay.setGeneratedBy(Essay.GenerationSource.STUDENT); 


        Essay savedEssay = essayRepository.save(essay);
        return convertToDTO(savedEssay);
    }

    //文书删除
    public void deleteEssay(Long essayId) {
        Essay essay = essayRepository.findById(essayId)
            .orElseThrow(() -> new ResourceNotFoundException("Essay not found with id: " + essayId));
        essayRepository.delete(essay);

    }
    

    public List<EssayDTO> getUserEssays(Long userId) {
        List<Essay> essays = essayRepository.findByUserId(userId);
        return essays.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    

    public EssayDTO getEssayById(Long essayId) {
        Optional<Essay> essay = essayRepository.findById(essayId);
        return essay.map(this::convertToDTO).orElse(null);
    }

    

    

} 