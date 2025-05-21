package com.admitgenius.backend.controller;

import com.admitgenius.backend.dto.CreateAdminRequestDTO;
import com.admitgenius.backend.dto.UserDTO;
import com.admitgenius.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal") // 特殊的路径前缀
public class InternalAdminController {

    @Autowired
    private UserService userService;

    /**
     * 内部接口：创建管理员账户
     * 注意：此接口应受严格保护（例如，通过API密钥、IP白名单等）
     * 
     * @param adminRequestDTO 包含管理员邮箱和密码的请求体
     * @return 创建成功的管理员用户信息
     */
    @PostMapping("/create-admin")
    public ResponseEntity<UserDTO> createAdminAccount(@Valid @RequestBody CreateAdminRequestDTO adminRequestDTO) {
        // 实际的安全校验（如API Key）应在拦截器/过滤器中完成
        UserDTO createdAdmin = userService.createAdminUser(adminRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAdmin);
    }
} 