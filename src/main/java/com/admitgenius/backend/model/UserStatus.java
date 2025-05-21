package com.admitgenius.backend.model;

/**
 * Defines the possible statuses of a user account.
 */
public enum UserStatus {
    ACTIVE,                // 账户正常 (Account is active)
    INACTIVE,              // 账户未激活或已停用 (Account is inactive or deactivated)
    SUSPENDED,             // 账户被暂停 (Account is suspended)
    PENDING_VERIFICATION   // 账户待验证（例如，邮箱验证） (Account pending verification)
} 