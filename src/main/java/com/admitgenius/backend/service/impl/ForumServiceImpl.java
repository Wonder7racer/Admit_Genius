package com.admitgenius.backend.service.impl;

import com.admitgenius.backend.dto.CommentDTO;
import com.admitgenius.backend.dto.ForumPostDTO;
import com.admitgenius.backend.model.*;
import com.admitgenius.backend.repository.CommentRepository;
import com.admitgenius.backend.repository.ForumRepository;
import com.admitgenius.backend.repository.PostLikeRepository;
import com.admitgenius.backend.repository.UserRepository;
import com.admitgenius.backend.service.ForumService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ForumServiceImpl implements ForumService {

    @Autowired
    private ForumRepository forumRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ForumPostDTO> getAllPosts(Pageable pageable, String keyword) {
        Page<ForumPost> postPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            postPage = forumRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable);
        } else {
            postPage = forumRepository.findAll(pageable);
        }
        return postPage.map(post -> convertToDTO(post, null));
    }

    @Override
    @Transactional
    public ForumPostDTO createPost(ForumPostDTO postDTO, Long userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在，ID: " + userId));

        // 权限检查
        if (UserRole.SCHOOL_ASSISTANT.equals(author.getRole())) {
            throw new AccessDeniedException("择校助手无权创建帖子");
        }

        // 输入验证
        if (postDTO == null) {
            throw new IllegalArgumentException("帖子信息不能为空");
        }
        if (postDTO.getTitle() == null || postDTO.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("帖子标题不能为空");
        }
        if (postDTO.getContent() == null || postDTO.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("帖子内容不能为空");
        }

        ForumPost post = new ForumPost();
        post.setTitle(postDTO.getTitle().trim());
        post.setContent(postDTO.getContent().trim());
        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setLikes(0);

        ForumPost savedPost = forumRepository.save(post);
        return convertToDTO(savedPost, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public ForumPostDTO getPostById(Long postId, Long userId) {
        ForumPost post = forumRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("帖子不存在，ID: " + postId));
        return convertToDTOWithComments(post, userId);
    }

    @Override
    @Transactional
    public CommentDTO addComment(Long postId, CommentDTO commentDTO, Long userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在，ID: " + userId));
        ForumPost post = forumRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("帖子不存在，ID: " + postId));

        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setAuthor(author);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setLikes(0);

        Comment savedComment = commentRepository.save(comment);
        
        post.setUpdatedAt(LocalDateTime.now()); 
        forumRepository.save(post);

        return convertToDTO(savedComment, userId);
    }

    @Override
    @Transactional
    public ForumPostDTO togglePostLike(Long postId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在，ID: " + userId));
        ForumPost post = forumRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("帖子不存在，ID: " + postId));

        Optional<PostLike> existingLike = postLikeRepository.findByUserAndPost(user, post);

        if (existingLike.isPresent()) {
            postLikeRepository.delete(existingLike.get());
        } else {
            PostLike newLike = new PostLike(user, post);
            postLikeRepository.save(newLike);
        }
        
        long currentLikes = postLikeRepository.countByPost(post);
        post.setLikes((int) currentLikes);
        post.setUpdatedAt(LocalDateTime.now());
        ForumPost savedPost = forumRepository.save(post);
        
        return convertToDTO(savedPost, userId);
    }

    @Override
    @Transactional
    public ForumPostDTO updatePost(Long postId, ForumPostDTO postDTO, Long userId) {
        ForumPost post = forumRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("帖子不存在，ID: " + postId));
        User requestingUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在，ID: " + userId));

        // 权限检查：择校助手不能修改帖子
        if (UserRole.SCHOOL_ASSISTANT.equals(requestingUser.getRole())) {
            throw new AccessDeniedException("择校助手无权修改帖子");
        }
        
        // 权限检查：只有作者自己或管理员可以修改帖子
        if (!post.getAuthor().getId().equals(requestingUser.getId()) && 
            !UserRole.ADMIN.equals(requestingUser.getRole())) {
            throw new AccessDeniedException("只有帖子作者或管理员可以修改此帖子"); 
        }

        // 输入验证
        if (postDTO.getTitle() == null || postDTO.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("帖子标题不能为空");
        }
        if (postDTO.getContent() == null || postDTO.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("帖子内容不能为空");
        }

        post.setTitle(postDTO.getTitle().trim());
        post.setContent(postDTO.getContent().trim());
        post.setUpdatedAt(LocalDateTime.now());

        ForumPost updatedPost = forumRepository.save(post);
        return convertToDTO(updatedPost, userId);
    }

    @Override
    @Transactional
    public void deletePost(Long postId, Long userId) {
        ForumPost post = forumRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("帖子不存在，ID: " + postId));
        User requestingUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在，ID: " + userId));

        if (UserRole.SCHOOL_ASSISTANT.equals(requestingUser.getRole())) {
            throw new AccessDeniedException("择校助手无权删除帖子");
        }
        if (!post.getAuthor().getId().equals(requestingUser.getId()) && !UserRole.ADMIN.equals(requestingUser.getRole())) {
            throw new AccessDeniedException("用户无权删除此帖子");
        }
        
        commentRepository.deleteByPost(post); 
        forumRepository.delete(post);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("评论不存在，ID: " + commentId));
        User requestingUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在，ID: " + userId));

        if (!comment.getAuthor().getId().equals(requestingUser.getId()) && !UserRole.ADMIN.equals(requestingUser.getRole())) {
            throw new AccessDeniedException("用户无权删除此评论");
        }

        ForumPost post = comment.getPost();
        commentRepository.delete(comment);

        if (post != null) {
            post.setUpdatedAt(LocalDateTime.now());
            forumRepository.save(post);
        }
    }

    // --- Helper Methods ---

    private ForumPostDTO convertToDTO(ForumPost post, Long currentUserId) {
        ForumPostDTO dto = new ForumPostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setLikes((int) postLikeRepository.countByPost(post));

        if (post.getAuthor() != null) {
            dto.setAuthorId(post.getAuthor().getId());
            String authorName = post.getAuthor().getFullName();
            if (authorName == null || authorName.trim().isEmpty()) {
                authorName = post.getAuthor().getEmail();
            }
            dto.setAuthorName(authorName);
        }
        dto.setCommentCount((int) commentRepository.countByPost(post));

        if (currentUserId != null) {
            User currentUser = userRepository.findById(currentUserId).orElse(null);
            if (currentUser != null) {
                dto.setLikedByCurrentUser(postLikeRepository.findByUserAndPost(currentUser, post).isPresent());
            }
        }
        return dto;
    }

    private ForumPostDTO convertToDTOWithComments(ForumPost post, Long currentUserId) {
        ForumPostDTO dto = convertToDTO(post, currentUserId); 
        List<Comment> comments = commentRepository.findByPostOrderByCreatedAtAsc(post);
        dto.setComments(comments.stream()
                                 .map(comment -> convertToDTO(comment, currentUserId))
                                 .collect(Collectors.toList()));
        dto.setCommentCount(dto.getComments().size());
        return dto;
    }

    private CommentDTO convertToDTO(Comment comment, Long currentUserId) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setLikes(comment.getLikes());
        if (comment.getPost() != null) {
            dto.setPostId(comment.getPost().getId());
        }
        if (comment.getAuthor() != null) {
            String authorName = comment.getAuthor().getFullName();
            if (authorName == null || authorName.trim().isEmpty()) {
                authorName = comment.getAuthor().getEmail();
            }
            dto.setAuthorId(comment.getAuthor().getId());
            dto.setAuthorName(authorName);
        }
        return dto;
    }
} 