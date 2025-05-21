package com.admitgenius.backend.service;

import com.admitgenius.backend.dto.UserDTO;
import com.admitgenius.backend.model.User;
import com.admitgenius.backend.model.UserRole;
import com.admitgenius.backend.model.UserStatus;
import com.admitgenius.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public UserDTO register(UserDTO userDTO) {
        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }
        
        User user = convertToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }
    
    public UserDTO login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 检查用户状态
        if (user.getStatus() == UserStatus.SUSPENDED) {
            throw new RuntimeException("账户已被禁用"); // 或者更具体的 DisabledException/LockedException
        }
        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new RuntimeException("账户未激活或已停用");
        }
        // PENDING_VERIFICATION 也可以根据业务逻辑决定是否允许登录
        if (user.getStatus() == UserStatus.PENDING_VERIFICATION) {
             // throw new RuntimeException("账户待验证");
        }
        
        return convertToDTO(user);
    }
    
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在 ID: " + id));
        return convertToDTO(user);
    }
    
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在 ID: " + id));
        
        // 更新用户信息
        user.setFullName(userDTO.getFullName());
        user.setProfilePicture(userDTO.getProfilePicture());
        user.setCurrentSchool(userDTO.getUndergraduateSchool());
        user.setGpa(userDTO.getGpa());
        
        // 更新考试成绩
        if (userDTO.getGreScore() != null) {
            user.setGreCombined(userDTO.getGreScore());
        }
        
        if (userDTO.getGmatScore() != null) {
            // 处理 gmatScore（实际字段可能不同）
            try {
                user.getClass().getDeclaredField("gmatTotal"); // 检查字段是否存在
                user.getClass().getDeclaredMethod("setGmatTotal", Integer.class)
                    .invoke(user, userDTO.getGmatScore());
            } catch (Exception e) {
                System.out.println("设置 GMAT 分数时出错: " + e.getMessage());
            }
        }
        
        if (userDTO.getToeflScore() != null) {
            user.setToeflScore(userDTO.getToeflScore());
        }
        
        if (userDTO.getIeltsScore() != null) {
            user.setIeltsScore(userDTO.getIeltsScore());
        }
        
        if (userDTO.getTargetMajor() != null) {
            user.setTargetMajor(userDTO.getTargetMajor());
        }
        
        // The updatedAt field should be updated automatically via @PreUpdate in User entity
        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }
    
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("用户不存在，邮箱: " + email));
        return convertToDTO(user);
    }
    
    /**
     * 删除用户
     * 管理员功能，用于删除指定ID的用户
     * 
     * @param id 用户ID
     * @throws RuntimeException 如果用户不存在
     */
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在 ID: " + id));
        
        // 执行删除操作
        userRepository.delete(user);
    }
    
    public UserDTO updateUserRole(Long userId, UserRole newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在，ID: " + userId));
        
        if (newRole == UserRole.ADMIN || user.getRole() == UserRole.ADMIN && newRole != UserRole.ADMIN) {
            throw new AccessDeniedException("不允许通过此接口直接操作ADMIN角色。");
        }

        user.setRole(newRole);
        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }
    
    public UserDTO suspendUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在，ID: " + userId));

        // 防止禁用ADMIN账户，除非有更高级别的逻辑允许
        if (user.getRole() == UserRole.ADMIN) {
            throw new AccessDeniedException("不允许通过此接口禁用ADMIN账户。");
        }

        user.setStatus(UserStatus.SUSPENDED);
        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    public UserDTO unsuspendUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在，ID: " + userId));
        
        // 一般来说，恢复账户时不需要对角色做特殊限制
        user.setStatus(UserStatus.ACTIVE);
        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    public void adminResetUserPassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在，ID: " + userId));

        // 通常不允许管理员直接重置其他ADMIN账户的密码，除非有特殊授权
        // 如果需要此功能，可以添加更复杂的权限检查或移除此限制
        if (user.getRole() == UserRole.ADMIN) {
            // 可以考虑允许重置自己的密码，或者需要一个特殊的权限来重置其他管理员密码
            // 此处简化为不允许通过此接口重置任何ADMIN密码
            throw new AccessDeniedException("不允许通过此接口重置ADMIN账户的密码。");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user); // 只需要保存，不需要返回DTO，因为密码不应该在DTO中泄露
    }
    
    public UserDTO createAdminUser(com.admitgenius.backend.dto.CreateAdminRequestDTO adminRequestDTO) {
        // 校验管理员密钥
        if (!"20031227".equals(adminRequestDTO.getAdminSecret())) {
            throw new org.springframework.security.access.AccessDeniedException("无效的管理员密钥");
        }

        if (userRepository.existsByEmail(adminRequestDTO.getEmail())) {
            throw new RuntimeException("邮箱已被注册: " + adminRequestDTO.getEmail());
        }
        User adminUser = new User();
        adminUser.setEmail(adminRequestDTO.getEmail());
        adminUser.setPassword(passwordEncoder.encode(adminRequestDTO.getPassword())); // 新管理员的密码
        adminUser.setRole(UserRole.ADMIN);
        adminUser.setFullName("Administrator (Secret Provisioned)"); 
        // status 默认为 ACTIVE， createdAt 和 updatedAt 会自动处理

        User savedAdmin = userRepository.save(adminUser);
        return convertToDTO(savedAdmin);
    }
    
    // 转换方法
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setProfilePicture(user.getProfilePicture());
        if (user.getRole() != null) {
           dto.setRole(user.getRole().name());
        }
        dto.setUndergraduateSchool(user.getCurrentSchool());
        dto.setGpa(user.getGpa());
        dto.setGreScore(user.getGreCombined());
        dto.setToeflScore(user.getToeflScore());
        dto.setIeltsScore(user.getIeltsScore());
        dto.setTargetMajor(user.getTargetMajor());
        
        // 获取 gmatScore（实际字段可能不同）
        try {
            Object gmatTotal = user.getClass().getDeclaredMethod("getGmatTotal").invoke(user);
            if (gmatTotal != null) {
                dto.setGmatScore((Integer) gmatTotal);
            }
        } catch (Exception e) {
            System.out.println("获取 GMAT 分数时出错: " + e.getMessage());
        }
        
        return dto;
    }
    
    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setProfilePicture(userDTO.getProfilePicture());
        user.setCurrentSchool(userDTO.getUndergraduateSchool());
        user.setGpa(userDTO.getGpa());
        user.setGreCombined(userDTO.getGreScore());
        user.setToeflScore(userDTO.getToeflScore());
        user.setIeltsScore(userDTO.getIeltsScore());
        user.setTargetMajor(userDTO.getTargetMajor());
        
        // 设置 gmatScore（实际字段可能不同）
        if (userDTO.getGmatScore() != null) {
            try {
                user.getClass().getDeclaredField("gmatTotal"); // 检查字段是否存在
                user.getClass().getDeclaredMethod("setGmatTotal", Integer.class)
                    .invoke(user, userDTO.getGmatScore());
            } catch (Exception e) {
                System.out.println("设置 GMAT 分数时出错: " + e.getMessage());
            }
        }
        
        try {
            if (userDTO.getRole() != null && !userDTO.getRole().isBlank()) {
                UserRole roleEnum = UserRole.valueOf(userDTO.getRole().trim().toUpperCase());
                user.setRole(roleEnum);
            } else {
                user.setRole(UserRole.USER);
            }
        } catch (IllegalArgumentException e) {
             System.err.println("Warning: Invalid role string in UserDTO: '" + userDTO.getRole() + "'. Defaulting to USER.");
             user.setRole(UserRole.USER);
        }
        
        return user;
    }
} 