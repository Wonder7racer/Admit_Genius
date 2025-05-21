package com.admitgenius.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRoleRequestDTO {
    @NotBlank(message = "角色不能为空")
    private String role; // 例如 "USER", "EXPERT", "SCHOOL_ASSISTANT", "ADMIN"
} 