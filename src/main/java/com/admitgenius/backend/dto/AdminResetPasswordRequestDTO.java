package com.admitgenius.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminResetPasswordRequestDTO {
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, message = "密码长度至少为6位") // 根据您的密码策略调整
    private String newPassword;
} 