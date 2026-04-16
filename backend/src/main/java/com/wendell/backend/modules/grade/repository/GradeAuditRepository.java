package com.wendell.backend.modules.grade.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wendell.backend.domain.GradeAudit;
import com.wendell.backend.modules.grade.dto.GradeAuditResponseDto;

@Repository
public interface GradeAuditRepository extends JpaRepository<GradeAudit, Long> {

    @Query("""
            SELECT new com.wendell.backend.modules.grade.dto.GradeAuditResponseDto(
                    ga.id,
                    s.id,
                    s.name,
                    e.id,
                    e.name,
                    ga.oldValue,
                    ga.newValue,
                    COALESCE(u.username, 'sistema'),
                    ga.modificationDate
            )
            FROM GradeAudit ga
            JOIN ga.student s
            JOIN ga.evaluation e
            LEFT JOIN ga.modifiedBy u
            WHERE s.id = :studentId
              AND ga.discipline.id = :disciplineId
            ORDER BY ga.modificationDate DESC
            """)
    List<GradeAuditResponseDto> findByStudentIdAndDisciplineId(
            @Param("studentId") Long studentId,
            @Param("disciplineId") Long disciplineId);
}
