package com.admitgenius.backend.repository;

import com.admitgenius.backend.model.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {
    List<School> findByRankingLessThanEqual(Integer maxRanking);
    List<School> findByAverageGPABetween(Double minGPA, Double maxGPA);
    List<School> findByAverageGMATBetween(Integer minGMAT, Integer maxGMAT);
} 