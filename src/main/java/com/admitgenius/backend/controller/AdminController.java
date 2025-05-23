package com.admitgenius.backend.controller;

import com.admitgenius.backend.dto.*;
import com.admitgenius.backend.model.UserRole;
import com.admitgenius.backend.service.SchoolService;
import com.admitgenius.backend.service.UserService;
import com.admitgenius.backend.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private AuthUtil authUtil;

    // --- 用户管理 ---

    /**
     * 获取所有用户列表
     * GET /api/admin/users
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * 创建管理员账户
     * POST /api/admin/users/create-admin
     */
    @PostMapping("/users/create-admin")
    public ResponseEntity<UserDTO> createAdmin(@Valid @RequestBody CreateAdminRequestDTO adminRequestDTO) {
        UserDTO createdAdmin = userService.createAdminUser(adminRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAdmin);
    }

    /**
     * 更新用户角色
     * PUT /api/admin/users/{userId}/role
     */
    @PutMapping("/users/{userId}/role")
    public ResponseEntity<UserDTO> updateUserRole(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRoleRequestDTO roleRequest) {
        UserRole newRole = UserRole.valueOf(roleRequest.getRole().toUpperCase());
        UserDTO updatedUser = userService.updateUserRole(userId, newRole);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * 重置用户密码
     * POST /api/admin/users/{userId}/reset-password
     */
    @PostMapping("/users/{userId}/reset-password")
    public ResponseEntity<Void> resetUserPassword(
            @PathVariable Long userId,
            @Valid @RequestBody AdminResetPasswordRequestDTO resetRequest) {
        userService.adminResetUserPassword(userId, resetRequest.getNewPassword());
        return ResponseEntity.ok().build();
    }

    /**
     * 暂停用户账户
     * POST /api/admin/users/{userId}/suspend
     */
    @PostMapping("/users/{userId}/suspend")
    public ResponseEntity<UserDTO> suspendUser(@PathVariable Long userId) {
        UserDTO suspendedUser = userService.suspendUser(userId);
        return ResponseEntity.ok(suspendedUser);
    }

    /**
     * 恢复用户账户
     * POST /api/admin/users/{userId}/unsuspend
     */
    @PostMapping("/users/{userId}/unsuspend")
    public ResponseEntity<UserDTO> unsuspendUser(@PathVariable Long userId) {
        UserDTO unsuspendedUser = userService.unsuspendUser(userId);
        return ResponseEntity.ok(unsuspendedUser);
    }

    /**
     * 删除用户
     * DELETE /api/admin/users/{userId}
     */
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    // --- 学校管理 ---

    /**
     * 获取所有学校
     * GET /api/admin/schools
     */
    @GetMapping("/schools")
    public ResponseEntity<List<SchoolDTO>> getAllSchools() {
        List<SchoolDTO> schools = schoolService.getAllSchools();
        return ResponseEntity.ok(schools);
    }

    /**
     * 创建学校
     * POST /api/admin/schools
     */
    @PostMapping("/schools")
    public ResponseEntity<SchoolDTO> createSchool(@Valid @RequestBody SchoolDTO schoolDTO) {
        Long currentUserId = authUtil.getCurrentUserId();
        SchoolDTO createdSchool = schoolService.addSchool(schoolDTO, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSchool);
    }

    /**
     * 更新学校信息
     * PUT /api/admin/schools/{id}
     */
    @PutMapping("/schools/{id}")
    public ResponseEntity<SchoolDTO> updateSchool(
            @PathVariable Long id,
            @Valid @RequestBody SchoolDTO schoolDTO) {
        Long currentUserId = authUtil.getCurrentUserId();
        SchoolDTO updatedSchool = schoolService.updateSchool(id, schoolDTO, currentUserId);
        return ResponseEntity.ok(updatedSchool);
    }

    /**
     * 删除学校
     * DELETE /api/admin/schools/{id}
     */
    @DeleteMapping("/schools/{id}")
    public ResponseEntity<Void> deleteSchool(@PathVariable Long id) {
        Long currentUserId = authUtil.getCurrentUserId();
        schoolService.deleteSchool(id, currentUserId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取学校详情
     * GET /api/admin/schools/{id}
     */
    @GetMapping("/schools/{id}")
    public ResponseEntity<SchoolDTO> getSchoolById(@PathVariable Long id) {
        SchoolDTO school = schoolService.getSchoolById(id);
        return ResponseEntity.ok(school);
    }

    // --- 学校项目管理 ---

    /**
     * 为学校添加项目
     * POST /api/admin/schools/{schoolId}/programs
     */
    @PostMapping("/schools/{schoolId}/programs")
    public ResponseEntity<SchoolProgramDTO> addSchoolProgram(
            @PathVariable Long schoolId,
            @Valid @RequestBody SchoolProgramDTO programDTO) {
        Long currentUserId = authUtil.getCurrentUserId();
        SchoolProgramDTO createdProgram = schoolService.addSchoolProgram(schoolId, programDTO, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProgram);
    }

    /**
     * 更新学校项目
     * PUT /api/admin/schools/programs/{programId}
     */
    @PutMapping("/schools/programs/{programId}")
    public ResponseEntity<SchoolProgramDTO> updateSchoolProgram(
            @PathVariable Long programId,
            @Valid @RequestBody SchoolProgramDTO programDTO) {
        Long currentUserId = authUtil.getCurrentUserId();
        SchoolProgramDTO updatedProgram = schoolService.updateSchoolProgram(programId, programDTO, currentUserId);
        return ResponseEntity.ok(updatedProgram);
    }

    /**
     * 删除学校项目
     * DELETE /api/admin/schools/programs/{programId}
     */
    @DeleteMapping("/schools/programs/{programId}")
    public ResponseEntity<Void> deleteSchoolProgram(@PathVariable Long programId) {
        Long currentUserId = authUtil.getCurrentUserId();
        schoolService.deleteSchoolProgram(programId, currentUserId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取学校的所有项目
     * GET /api/admin/schools/{schoolId}/programs
     */
    @GetMapping("/schools/{schoolId}/programs")
    public ResponseEntity<List<SchoolProgramDTO>> getSchoolPrograms(@PathVariable Long schoolId) {
        List<SchoolProgramDTO> programs = schoolService.getAllProgramsBySchool(schoolId);
        return ResponseEntity.ok(programs);
    }
} 