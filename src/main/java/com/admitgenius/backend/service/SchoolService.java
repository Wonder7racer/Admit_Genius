package com.admitgenius.backend.service;

import com.admitgenius.backend.dto.SchoolDTO;
import com.admitgenius.backend.dto.SchoolProgramDTO;
import java.util.List;

/**
 * 学校服务接口
 * 处理学校相关的业务逻辑
 */
public interface SchoolService {
    /**
     * 添加新学校
     * @param schoolDTO 学校信息
     * @param userId 请求操作的用户ID
     * @return 保存后的学校信息（包含ID）
     */
    SchoolDTO addSchool(SchoolDTO schoolDTO, Long userId);
    
    /**
     * 更新学校信息
     * @param id 学校ID
     * @param schoolDTO 更新的学校信息
     * @param userId 请求操作的用户ID
     * @return 更新后的学校信息
     */
    SchoolDTO updateSchool(Long id, SchoolDTO schoolDTO, Long userId);
    
    /**
     * 删除学校
     * @param id 学校ID
     * @param userId 请求操作的用户ID
     */
    void deleteSchool(Long id, Long userId);
    
    /**
     * 获取所有学校列表
     * @return 学校列表
     */
    List<SchoolDTO> getAllSchools();
    
    /**
     * 根据ID获取学校详情
     * @param id 学校ID
     * @return 学校详情
     */
    SchoolDTO getSchoolById(Long id);

    // --- SchoolProgram Management ---

    /**
     * 为指定学校添加新项目
     * @param schoolId 学校ID
     * @param programDTO 项目信息
     * @param userId 请求操作的用户ID
     * @return 保存后的项目信息
     */
    SchoolProgramDTO addSchoolProgram(Long schoolId, SchoolProgramDTO programDTO, Long userId);

    /**
     * 更新项目信息
     * @param programId 项目ID
     * @param programDTO 更新的项目信息
     * @param userId 请求操作的用户ID
     * @return 更新后的项目信息
     */
    SchoolProgramDTO updateSchoolProgram(Long programId, SchoolProgramDTO programDTO, Long userId);

    /**
     * 删除项目
     * @param programId 项目ID
     * @param userId 请求操作的用户ID
     */
    void deleteSchoolProgram(Long programId, Long userId);

    /**
     * 根据ID获取项目详情
     * @param programId 项目ID
     * @return 项目详情
     */
    SchoolProgramDTO getSchoolProgramById(Long programId);

    /**
     * 获取指定学校的所有项目列表
     * @param schoolId 学校ID
     * @return 项目列表
     */
    List<SchoolProgramDTO> getAllProgramsBySchool(Long schoolId);
} 