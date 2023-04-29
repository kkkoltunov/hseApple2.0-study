package com.hseapple.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SubjectDao extends JpaRepository<SubjectEntity, Long> {
    @Transactional
    void deleteSubjectById(Long id);
}