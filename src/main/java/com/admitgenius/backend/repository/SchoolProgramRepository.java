package com.admitgenius.backend.repository;

import com.admitgenius.backend.model.School;
import com.admitgenius.backend.model.SchoolProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolProgramRepository extends JpaRepository<SchoolProgram, Long> {
    List<SchoolProgram> findBySchool(School school);
    
    List<SchoolProgram> findBySchoolId(Long schoolId);
    
    List<SchoolProgram> findByDegreeLevel(SchoolProgram.DegreeLevel degreeLevel);
    
    @Query("SELECT p FROM SchoolProgram p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "EXISTS (SELECT k FROM p.keywords k WHERE LOWER(k) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<SchoolProgram> findByNameOrKeywordsContaining(String keyword);

    void deleteBySchoolId(Long schoolId);
}