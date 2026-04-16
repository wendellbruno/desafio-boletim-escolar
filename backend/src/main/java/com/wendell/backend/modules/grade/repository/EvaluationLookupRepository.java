package com.wendell.backend.modules.grade.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wendell.backend.domain.Evaluation;

@Repository
public interface EvaluationLookupRepository extends JpaRepository<Evaluation, Long> {
}
