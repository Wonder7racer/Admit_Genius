package com.admitgenius.backend.repository;

import com.admitgenius.backend.model.ForumPost;
import com.admitgenius.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumRepository extends JpaRepository<ForumPost, Long> {
    Page<ForumPost> findByAuthor(User author, Pageable pageable);
    Page<ForumPost> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String titleKeyword, String contentKeyword, Pageable pageable);
} 