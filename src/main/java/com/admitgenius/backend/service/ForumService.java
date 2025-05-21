package com.admitgenius.backend.service;

import com.admitgenius.backend.dto.CommentDTO;
import com.admitgenius.backend.dto.ForumPostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ForumService {

    /**
     * 获取帖子列表（支持分页和关键字搜索）
     * @param pageable 分页信息
     * @param keyword 搜索关键字（标题或内容）
     * @return 帖子DTO的分页结果
     */
    Page<ForumPostDTO> getAllPosts(Pageable pageable, String keyword);

    /**
     * 创建新帖子
     * @param postDTO 包含帖子标题和内容的DTO
     * @param userId 发帖用户ID
     * @return 创建后的帖子DTO
     */
    ForumPostDTO createPost(ForumPostDTO postDTO, Long userId);

    /**
     * 根据ID获取帖子详情
     * @param postId 帖子ID
     * @param userId 当前查看用户的ID (用于判断是否点赞等)
     * @return 帖子DTO
     */
    ForumPostDTO getPostById(Long postId, Long userId);

    /**
     * 为帖子添加评论
     * @param postId 帖子ID
     * @param commentDTO 包含评论内容的DTO
     * @param userId 评论用户ID
     * @return 创建后的评论DTO
     */
    CommentDTO addComment(Long postId, CommentDTO commentDTO, Long userId);

    /**
     * 点赞或取消点赞帖子
     * @param postId 帖子ID
     * @param userId 点赞/取消点赞的用户ID
     * @return 更新点赞状态和数量后的帖子DTO
     */
    ForumPostDTO togglePostLike(Long postId, Long userId);

    /**
     * 更新帖子
     * @param postId 帖子ID
     * @param postDTO 包含更新后帖子标题和内容的DTO
     * @param userId 请求更新的用户ID（用于权限校验）
     * @return 更新后的帖子DTO
     */
    ForumPostDTO updatePost(Long postId, ForumPostDTO postDTO, Long userId);

    /**
     * 删除帖子
     * @param postId 帖子ID
     * @param userId 请求删除的用户ID（用于权限校验，或判断是否为管理员）
     */
    void deletePost(Long postId, Long userId);

    /**
     * 删除评论
     * @param commentId 评论ID
     * @param userId 请求删除的用户ID（用于权限校验，或判断是否为管理员）
     */
    void deleteComment(Long commentId, Long userId);
} 