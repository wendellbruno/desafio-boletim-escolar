package com.wendell.backend.modules.grade.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wendell.backend.domain.Grade;
import com.wendell.backend.modules.grade.dto.GradeListingResponseDto;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    @Query("""
            SELECT new com.wendell.backend.modules.grade.dto.GradeListingResponseDto(
                    g.id,
                    s.id,
                    s.name,
                    c.id,
                    c.name,
                    d.id,
                    d.name,
                    e.id,
                    e.name,
                    e.weight,
                    g.grade_value
            )
            FROM Grade g
            JOIN g.student s
            JOIN s.classroom c
            JOIN g.evaluation e
            JOIN e.discipline d
            WHERE (:classroomId IS NULL OR c.id = :classroomId)
              AND (:disciplineId IS NULL OR d.id = :disciplineId)
            """)
    List<GradeListingResponseDto> findAllByClassroomIdAndDisciplineId(
            @Param("classroomId") Long classroomId,
            @Param("disciplineId") Long disciplineId);

    java.util.Optional<Grade> findByStudentIdAndEvaluationId(Long studentId, Long evaluationId);

    @Query("""
            SELECT s.classroom.id
            FROM Student s
            WHERE s.id = :studentId
            """)
    Optional<Long> findClassroomIdByStudentId(@Param("studentId") Long studentId);

    @Query("""
            SELECT e.discipline.id
            FROM Evaluation e
            WHERE e.id = :evaluationId
            """)
    Optional<Long> findDisciplineIdByEvaluationId(@Param("evaluationId") Long evaluationId);
}
