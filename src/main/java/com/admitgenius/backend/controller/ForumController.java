package com.admitgenius.backend.controller;

import com.admitgenius.backend.dto.CommentDTO;
import com.admitgenius.backend.dto.ForumPostDTO;
import com.admitgenius.backend.dto.UserDTO;
import com.admitgenius.backend.service.ForumService;
import com.admitgenius.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forum")
public class ForumController {
    @Autowired
    private ForumService forumService;
    
    @Autowired
    private UserService userService;
    
    // 辅助方法，从 Authentication 对象获取用户ID (与AdminController中的类似)
    private Long getUserIdFromAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            // 对于某些公共接口，可能允许匿名访问，此时userId可以为null
            return null; 
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            UserDTO user = userService.findByEmail(username); 
            if (user == null) {
                 throw new RuntimeException("无法根据认证信息找到用户: " + username);
            }
            return user.getId();
        } else if (principal instanceof String && !"anonymousUser".equals(principal)) {
             // 避免将 "anonymousUser" 字符串传递给 findByEmail
            UserDTO user = userService.findByEmail((String)principal); 
             if (user == null) {
                 throw new RuntimeException("无法根据认证信息找到用户: " + principal);
            }
            return user.getId();
        } else if ("anonymousUser".equals(principal)) {
            return null; // 明确处理匿名用户
        }
        // 如果 principal 不是预期的类型，也视为无法获取用户ID，返回null或抛出异常
        // throw new IllegalStateException("无法从认证主体获取用户ID: " + principal.getClass().getName());
        return null; 
    }
    
    // 获取帖子列表，支持分页和搜索
    @GetMapping("/posts")
    public ResponseEntity<Page<ForumPostDTO>> getAllPosts(
            @PageableDefault(size = 10, sort = "createdAt,desc") Pageable pageable, 
            @RequestParam(required = false) String keyword,
            Authentication authentication) {
        // Long currentUserId = getUserIdFromAuthentication(authentication); // getAllPosts 在 service 层目前不需要 userId
        Page<ForumPostDTO> posts = forumService.getAllPosts(pageable, keyword);
        return ResponseEntity.ok(posts);
    }
    
    // 创建帖子 (需要认证，userId应从安全上下文中获取)
    @PostMapping("/posts")
    public ResponseEntity<ForumPostDTO> createPost(@RequestBody ForumPostDTO postDTO, Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 或者抛出异常
        }
        ForumPostDTO createdPost = forumService.createPost(postDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }
    
    // 获取帖子详情
    @GetMapping("/posts/{postId}")
    public ResponseEntity<ForumPostDTO> getPostById(@PathVariable Long postId, Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication); // userId 可以为 null，表示匿名查看
        ForumPostDTO post = forumService.getPostById(postId, userId);
        return ResponseEntity.ok(post);
    }
    
    // 发表评论 (需要认证，userId应从安全上下文中获取)
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long postId, 
                                                 @RequestBody CommentDTO commentDTO, 
                                                 Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CommentDTO createdComment = forumService.addComment(postId, commentDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }
    
    // 点赞帖子 (需要认证)
    @PostMapping("/posts/{postId}/toggle-like")
    public ResponseEntity<ForumPostDTO> togglePostLike(@PathVariable Long postId, Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ForumPostDTO updatedPost = forumService.togglePostLike(postId, userId);
        return ResponseEntity.ok(updatedPost);
    }
} 