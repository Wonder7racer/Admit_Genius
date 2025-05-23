package com.admitgenius.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/system")
public class SystemController {

    @Autowired
    private DataSource dataSource;

    /**
     * 健康检查端点
     * @return 系统健康状态
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("service", "AdmitGenius Backend");
        health.put("version", "0.0.1-SNAPSHOT");
        
        // 检查数据库连接
        try (Connection connection = dataSource.getConnection()) {
            health.put("database", "UP");
        } catch (Exception e) {
            health.put("database", "DOWN");
            health.put("database_error", e.getMessage());
        }
        
        return ResponseEntity.ok(health);
    }

    /**
     * 系统信息端点
     * @return 系统详细信息
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> systemInfo() {
        Map<String, Object> info = new HashMap<>();
        
        // 应用信息
        info.put("application", Map.of(
            "name", "AdmitGenius Backend",
            "version", "0.0.1-SNAPSHOT",
            "description", "智能留学申请助手系统后端服务"
        ));
        
        // 系统信息
        Runtime runtime = Runtime.getRuntime();
        info.put("system", Map.of(
            "java_version", System.getProperty("java.version"),
            "os_name", System.getProperty("os.name"),
            "os_version", System.getProperty("os.version"),
            "available_processors", runtime.availableProcessors(),
            "max_memory", runtime.maxMemory() / 1024 / 1024 + " MB",
            "total_memory", runtime.totalMemory() / 1024 / 1024 + " MB",
            "free_memory", runtime.freeMemory() / 1024 / 1024 + " MB"
        ));
        
        // 功能模块
        info.put("features", Map.of(
            "user_management", "用户注册、登录、JWT认证",
            "ai_essay_system", "基于OpenAI GPT的文书生成与润色",
            "recommendation_system", "智能学校推荐算法",
            "forum_system", "论坛交流功能",
            "admin_system", "管理员后台管理"
        ));
        
        return ResponseEntity.ok(info);
    }

    /**
     * API版本信息
     * @return API版本详情
     */
    @GetMapping("/version")
    public ResponseEntity<Map<String, Object>> apiVersion() {
        Map<String, Object> version = new HashMap<>();
        version.put("api_version", "v1");
        version.put("build_time", LocalDateTime.now());
        version.put("spring_boot_version", "3.4.3");
        version.put("java_version", "17");
        
        return ResponseEntity.ok(version);
    }
} 