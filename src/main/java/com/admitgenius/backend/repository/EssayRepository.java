package com.admitgenius.backend.repository;

import com.admitgenius.backend.model.Essay;
import com.admitgenius.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface EssayRepository extends JpaRepository<Essay, Long> {
    List<Essay> findByUser(User user);
    List<Essay> findByUserId(Long userId);
   
    Optional<Essay> findById(Long id); 



}

