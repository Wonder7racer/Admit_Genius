package com.admitgenius.backend.repository;

import com.admitgenius.backend.model.Comment;
import com.admitgenius.backend.model.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(ForumPost post);
    List<Comment> findByPostOrderByCreatedAtAsc(ForumPost post);
    long countByPost(ForumPost post);
    void deleteByPost(ForumPost post);
} 