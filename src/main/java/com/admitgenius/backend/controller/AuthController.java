package com.admitgenius.backend.controller;

import com.admitgenius.backend.model.User;
import com.admitgenius.backend.model.UserRole; // 需要确认 UserRole 枚举是否存在且路径正确
import com.admitgenius.backend.payload.ApiResponse; // 需要创建这个类用于通用响应
import com.admitgenius.backend.payload.JwtAuthenticationResponse;
import com.admitgenius.backend.payload.LoginRequest;
import com.admitgenius.backend.payload.SignUpRequest;
import com.admitgenius.backend.repository.UserRepository;
import com.admitgenius.backend.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成JWT令牌
        String jwt = tokenProvider.generateToken(authentication);
        
        // 获取用户信息
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 返回令牌和用户信息
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, user));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "邮箱已被注册!"),
                    HttpStatus.BAD_REQUEST);
        }

        // 创建新用户
        User user = new User();
        user.setFullName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        
        // 设置额外信息（如果提供）
        if (signUpRequest.getProfilePicture() != null) {
            user.setProfilePicture(signUpRequest.getProfilePicture());
        }
        
        if (signUpRequest.getUndergraduateSchool() != null) {
            user.setCurrentSchool(signUpRequest.getUndergraduateSchool());
        }
        
        if (signUpRequest.getGpa() != null) {
            user.setGpa(signUpRequest.getGpa());
        }
        
        if (signUpRequest.getGreScore() != null) {
            user.setGreCombined(signUpRequest.getGreScore());
        }
        
        // 根据User类中的实际字段名进行设置
        if (signUpRequest.getGmatScore() != null) {
            // 检查User中是否有相应字段，如果没有则忽略此设置
            try {
                user.getClass().getDeclaredField("gmatTotal"); // 检查字段是否存在
                // 如果字段存在，使用反射进行设置
                user.getClass().getDeclaredMethod("setGmatTotal", Integer.class)
                    .invoke(user, signUpRequest.getGmatScore());
            } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
                // 字段不存在，忽略设置
                System.out.println("User类中没有gmatTotal字段，忽略此设置");
            }
        }

        // 设置角色
        if (signUpRequest.getRole() != null && !signUpRequest.getRole().isEmpty()) {
            try {
                user.setRole(UserRole.valueOf(signUpRequest.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                // 如果提供的角色无效，则使用默认USER角色
                user.setRole(UserRole.USER);
            }
        } else {
            // 设置默认角色为 USER
            user.setRole(UserRole.USER);
        }

        User result = userRepository.save(user);

        // 构建新创建资源的URI，使用用户ID
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{id}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "用户注册成功!"));
    }
} 