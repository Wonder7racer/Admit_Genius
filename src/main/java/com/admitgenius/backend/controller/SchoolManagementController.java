package com.admitgenius.backend.controller;

import com.admitgenius.backend.dto.SchoolDTO;
import com.admitgenius.backend.dto.SchoolProgramDTO;
import com.admitgenius.backend.service.SchoolService;
import com.admitgenius.backend.util.AuthUtil; // Assuming you have a utility to get current user ID
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/management/schools") // Base path for school management
public class SchoolManagementController {

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private AuthUtil authUtil; // Utility to get authenticated user's ID

    // --- School Management Endpoints ---

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SchoolDTO> addSchool(@Valid @RequestBody SchoolDTO schoolDTO) {
        Long currentUserId = authUtil.getCurrentUserId();
        SchoolDTO newSchool = schoolService.addSchool(schoolDTO, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSchool);
    }

    @PutMapping("/{schoolId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SCHOOL_ASSISTANT')")
    public ResponseEntity<SchoolDTO> updateSchool(@PathVariable Long schoolId, @Valid @RequestBody SchoolDTO schoolDTO) {
        Long currentUserId = authUtil.getCurrentUserId();
        SchoolDTO updatedSchool = schoolService.updateSchool(schoolId, schoolDTO, currentUserId);
        return ResponseEntity.ok(updatedSchool);
    }

    @DeleteMapping("/{schoolId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteSchool(@PathVariable Long schoolId) {
        Long currentUserId = authUtil.getCurrentUserId();
        schoolService.deleteSchool(schoolId, currentUserId);
        return ResponseEntity.noContent().build();
    }

    // GET all schools and GET school by ID are already in RecommendationController, 
    // but can be duplicated here if a different auth scheme or path is needed for management.
    // For now, we assume they are publicly accessible or have their own auth in RecommendationController.
    @GetMapping
    public ResponseEntity<List<SchoolDTO>> getAllSchools() {
        return ResponseEntity.ok(schoolService.getAllSchools());
    }

    @GetMapping("/{schoolId}")
    public ResponseEntity<SchoolDTO> getSchoolById(@PathVariable Long schoolId) {
        return ResponseEntity.ok(schoolService.getSchoolById(schoolId));
    }


    // --- SchoolProgram Management Endpoints ---

    @PostMapping("/{schoolId}/programs")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SchoolProgramDTO> addSchoolProgram(@PathVariable Long schoolId, @Valid @RequestBody SchoolProgramDTO programDTO) {
        Long currentUserId = authUtil.getCurrentUserId();
        SchoolProgramDTO newProgram = schoolService.addSchoolProgram(schoolId, programDTO, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProgram);
    }

    @PutMapping("/programs/{programId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SCHOOL_ASSISTANT')")
    public ResponseEntity<SchoolProgramDTO> updateSchoolProgram(@PathVariable Long programId, @Valid @RequestBody SchoolProgramDTO programDTO) {
        Long currentUserId = authUtil.getCurrentUserId();
        SchoolProgramDTO updatedProgram = schoolService.updateSchoolProgram(programId, programDTO, currentUserId);
        return ResponseEntity.ok(updatedProgram);
    }

    @DeleteMapping("/programs/{programId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteSchoolProgram(@PathVariable Long programId) {
        Long currentUserId = authUtil.getCurrentUserId();
        schoolService.deleteSchoolProgram(programId, currentUserId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{schoolId}/programs")
    public ResponseEntity<List<SchoolProgramDTO>> getAllProgramsBySchool(@PathVariable Long schoolId) {
        List<SchoolProgramDTO> programs = schoolService.getAllProgramsBySchool(schoolId);
        return ResponseEntity.ok(programs);
    }

    @GetMapping("/programs/{programId}")
    public ResponseEntity<SchoolProgramDTO> getSchoolProgramById(@PathVariable Long programId) {
        SchoolProgramDTO program = schoolService.getSchoolProgramById(programId);
        return ResponseEntity.ok(program);
    }
} 