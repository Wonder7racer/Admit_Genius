package com.admitgenius.backend.service.impl;

import com.admitgenius.backend.dto.SchoolDTO;
import com.admitgenius.backend.dto.SchoolProgramDTO;
import com.admitgenius.backend.exception.ResourceNotFoundException;
import com.admitgenius.backend.exception.UnauthorizedAccessException;
import com.admitgenius.backend.model.School;
import com.admitgenius.backend.model.SchoolProgram;
import com.admitgenius.backend.model.User;
import com.admitgenius.backend.model.UserRole;
import com.admitgenius.backend.repository.SchoolProgramRepository;
import com.admitgenius.backend.repository.SchoolRepository;
import com.admitgenius.backend.repository.UserRepository;
import com.admitgenius.backend.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SchoolServiceImpl implements SchoolService {

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private SchoolProgramRepository schoolProgramRepository;

    @Autowired
    private UserRepository userRepository;

    // --- School Management ---

    @Override
    @Transactional
    public SchoolDTO addSchool(SchoolDTO schoolDTO, Long userId) {
        User user = getUserById(userId);
        if (user.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedAccessException("只有管理员可以添加学校");
        }
        School school = convertToSchoolEntity(schoolDTO);
        school = schoolRepository.save(school);
        return convertToSchoolDTO(school);
    }

    @Override
    @Transactional
    public SchoolDTO updateSchool(Long id, SchoolDTO schoolDTO, Long userId) {
        User user = getUserById(userId);
        if (user.getRole() != UserRole.ADMIN && user.getRole() != UserRole.SCHOOL_ASSISTANT) {
            throw new UnauthorizedAccessException("只有管理员或择校助手可以修改学校信息");
        }
        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("未找到ID为 " + id + " 的学校"));
        
        // Update fields from DTO
        school.setName(schoolDTO.getName());
        school.setLocation(schoolDTO.getLocation());
        school.setRanking(schoolDTO.getRanking());
        school.setAcceptanceRate(schoolDTO.getAcceptanceRate());
        school.setAverageGREVerbal(schoolDTO.getAverageGREVerbal());
        school.setAverageGREQuant(schoolDTO.getAverageGREQuant());
        school.setAverageGREAW(schoolDTO.getAverageGREAW());
        school.setAverageGMAT(schoolDTO.getAverageGMAT());
        school.setAverageGPA(schoolDTO.getAverageGPA());
        school.setIsIvyLeague(schoolDTO.getIsIvyLeague());
        school.setDescription(schoolDTO.getDescription());
        school.setWebsite(schoolDTO.getWebsite());
        school.setImageUrl(schoolDTO.getImageUrl());
        school.setHasScholarship(schoolDTO.getHasScholarship());
        school.setTuitionFee(schoolDTO.getTuitionFee());
        school.setAdmissionRequirements(schoolDTO.getAdmissionRequirements());
        school.setTopPrograms(schoolDTO.getTopPrograms());

        school = schoolRepository.save(school);
        return convertToSchoolDTO(school);
    }

    @Override
    @Transactional
    public void deleteSchool(Long id, Long userId) {
        User user = getUserById(userId);
        if (user.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedAccessException("只有管理员可以删除学校");
        }
        if (!schoolRepository.existsById(id)) {
            throw new ResourceNotFoundException("未找到ID为 " + id + " 的学校");
        }
        // Consider handling related SchoolPrograms (e.g., cascade delete or disassociate)
        // For now, let's assume cascade delete is configured at DB or JPA level, or delete them manually if needed.
        schoolProgramRepository.deleteBySchoolId(id); // Example of manual deletion if not cascaded
        schoolRepository.deleteById(id);
    }

    @Override
    public List<SchoolDTO> getAllSchools() {
        return schoolRepository.findAll().stream().map(this::convertToSchoolDTO).collect(Collectors.toList());
    }

    @Override
    public SchoolDTO getSchoolById(Long id) {
        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("未找到ID为 " + id + " 的学校"));
        return convertToSchoolDTO(school);
    }

    // --- SchoolProgram Management ---

    @Override
    @Transactional
    public SchoolProgramDTO addSchoolProgram(Long schoolId, SchoolProgramDTO programDTO, Long userId) {
        User user = getUserById(userId);
        if (user.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedAccessException("只有管理员可以添加项目");
        }
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new ResourceNotFoundException("未找到ID为 " + schoolId + " 的学校以关联项目"));
        
        SchoolProgram program = convertToProgramEntity(programDTO, school);
        program = schoolProgramRepository.save(program);
        return convertToProgramDTO(program);
    }

    @Override
    @Transactional
    public SchoolProgramDTO updateSchoolProgram(Long programId, SchoolProgramDTO programDTO, Long userId) {
        User user = getUserById(userId);
        if (user.getRole() != UserRole.ADMIN && user.getRole() != UserRole.SCHOOL_ASSISTANT) {
            throw new UnauthorizedAccessException("只有管理员或择校助手可以修改项目信息");
        }
        SchoolProgram program = schoolProgramRepository.findById(programId)
                .orElseThrow(() -> new ResourceNotFoundException("未找到ID为 " + programId + " 的项目"));

        // Update fields from DTO
        program.setName(programDTO.getName());
        program.setDepartment(programDTO.getDepartment());
        if (programDTO.getDegreeLevel() != null) {
             try {
                program.setDegreeLevel(SchoolProgram.DegreeLevel.valueOf(programDTO.getDegreeLevel().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("无效的学位等级: " + programDTO.getDegreeLevel());
            }
        }
        program.setDuration(programDTO.getDuration());
        program.setTuitionFee(programDTO.getTuitionFee());
        program.setScholarshipAvailable(programDTO.getScholarshipAvailable());
        program.setAdmissionRequirements(programDTO.getAdmissionRequirements());
        program.setKeywords(programDTO.getKeywords());
        
        // If schoolId is provided and different, update the association (optional, can be complex)
        // For simplicity, we assume program's school doesn't change, or it's handled by deleting and re-adding.

        program = schoolProgramRepository.save(program);
        return convertToProgramDTO(program);
    }

    @Override
    @Transactional
    public void deleteSchoolProgram(Long programId, Long userId) {
        User user = getUserById(userId);
        if (user.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedAccessException("只有管理员可以删除项目");
        }
        if (!schoolProgramRepository.existsById(programId)) {
            throw new ResourceNotFoundException("未找到ID为 " + programId + " 的项目");
        }
        schoolProgramRepository.deleteById(programId);
    }

    @Override
    public SchoolProgramDTO getSchoolProgramById(Long programId) {
        SchoolProgram program = schoolProgramRepository.findById(programId)
                .orElseThrow(() -> new ResourceNotFoundException("未找到ID为 " + programId + " 的项目"));
        return convertToProgramDTO(program);
    }

    @Override
    public List<SchoolProgramDTO> getAllProgramsBySchool(Long schoolId) {
        if (!schoolRepository.existsById(schoolId)) {
             throw new ResourceNotFoundException("未找到ID为 " + schoolId + " 的学校");
        }
        List<SchoolProgram> programs = schoolProgramRepository.findBySchoolId(schoolId);
        return programs.stream().map(this::convertToProgramDTO).collect(Collectors.toList());
    }

    // --- Helper Methods ---

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("未找到ID为 " + userId + " 的用户"));
    }

    private SchoolDTO convertToSchoolDTO(School school) {
        SchoolDTO dto = new SchoolDTO();
        dto.setId(school.getId());
        dto.setName(school.getName());
        dto.setLocation(school.getLocation());
        dto.setRanking(school.getRanking());
        dto.setAcceptanceRate(school.getAcceptanceRate());
        dto.setAverageGREVerbal(school.getAverageGREVerbal());
        dto.setAverageGREQuant(school.getAverageGREQuant());
        dto.setAverageGREAW(school.getAverageGREAW());
        dto.setAverageGMAT(school.getAverageGMAT());
        dto.setAverageGPA(school.getAverageGPA());
        dto.setIsIvyLeague(school.getIsIvyLeague());
        dto.setDescription(school.getDescription());
        dto.setWebsite(school.getWebsite());
        dto.setImageUrl(school.getImageUrl());
        dto.setHasScholarship(school.getHasScholarship());
        dto.setTuitionFee(school.getTuitionFee());
        dto.setAdmissionRequirements(school.getAdmissionRequirements());
        dto.setTopPrograms(school.getTopPrograms());
        // Consider adding a list of SchoolProgramDTOs here if needed for detailed school view
        return dto;
    }

    private School convertToSchoolEntity(SchoolDTO dto) {
        School school = new School();
        // ID is auto-generated, so not set here
        school.setName(dto.getName());
        school.setLocation(dto.getLocation());
        school.setRanking(dto.getRanking());
        school.setAcceptanceRate(dto.getAcceptanceRate());
        school.setAverageGREVerbal(dto.getAverageGREVerbal());
        school.setAverageGREQuant(dto.getAverageGREQuant());
        school.setAverageGREAW(dto.getAverageGREAW());
        school.setAverageGMAT(dto.getAverageGMAT());
        school.setAverageGPA(dto.getAverageGPA());
        school.setIsIvyLeague(dto.getIsIvyLeague());
        school.setDescription(dto.getDescription());
        school.setWebsite(dto.getWebsite());
        school.setImageUrl(dto.getImageUrl());
        school.setHasScholarship(dto.getHasScholarship());
        school.setTuitionFee(dto.getTuitionFee());
        school.setAdmissionRequirements(dto.getAdmissionRequirements());
        school.setTopPrograms(dto.getTopPrograms());
        return school;
    }

    private SchoolProgramDTO convertToProgramDTO(SchoolProgram program) {
        SchoolProgramDTO dto = new SchoolProgramDTO();
        dto.setId(program.getId());
        if (program.getSchool() != null) {
            dto.setSchoolId(program.getSchool().getId());
            dto.setSchoolName(program.getSchool().getName());
        }
        dto.setName(program.getName());
        dto.setDepartment(program.getDepartment());
        if (program.getDegreeLevel() != null) {
            dto.setDegreeLevel(program.getDegreeLevel().name());
        }
        dto.setDuration(program.getDuration());
        dto.setTuitionFee(program.getTuitionFee());
        dto.setScholarshipAvailable(program.getScholarshipAvailable());
        dto.setAdmissionRequirements(program.getAdmissionRequirements());
        dto.setKeywords(program.getKeywords());
        return dto;
    }

    private SchoolProgram convertToProgramEntity(SchoolProgramDTO dto, School school) {
        SchoolProgram program = new SchoolProgram();
        // ID is auto-generated
        program.setSchool(school); // Associate with the school
        program.setName(dto.getName());
        program.setDepartment(dto.getDepartment());
        if (dto.getDegreeLevel() != null && !dto.getDegreeLevel().isEmpty()) {
            try {
                program.setDegreeLevel(SchoolProgram.DegreeLevel.valueOf(dto.getDegreeLevel().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("无效的学位等级: " + dto.getDegreeLevel() + ". 可用值: BACHELOR, MASTER, PHD");
            }
        }
        program.setDuration(dto.getDuration());
        program.setTuitionFee(dto.getTuitionFee());
        program.setScholarshipAvailable(dto.getScholarshipAvailable());
        program.setAdmissionRequirements(dto.getAdmissionRequirements());
        program.setKeywords(dto.getKeywords());
        return program;
    }
} 