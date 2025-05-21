package com.admitgenius.backend.repository;

import com.admitgenius.backend.model.ApplicationStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationStatisticRepository extends JpaRepository<ApplicationStatistic, Long> {
    List<ApplicationStatistic> findBySchoolIdOrderByYearDesc(Long schoolId);
    Optional<ApplicationStatistic> findFirstBySchoolIdOrderByYearDesc(Long schoolId);
    List<ApplicationStatistic> findByYearAndTerm(Integer year, String term);
} 