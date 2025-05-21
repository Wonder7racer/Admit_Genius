package com.admitgenius.backend.repository;

import com.admitgenius.backend.model.ForumPost;
import com.admitgenius.backend.model.PostLike;
import com.admitgenius.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, ForumPost post);
    long countByPost(ForumPost post);
    // 如果需要，还可以添加 existsByUserAndPost(User user, ForumPost post) 方法
} 