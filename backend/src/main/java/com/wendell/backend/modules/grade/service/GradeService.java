package com.wendell.backend.modules.grade.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wendell.backend.common.exception.BadRequestException;
import com.wendell.backend.common.security.AuthenticatedUserValidator;
import com.wendell.backend.domain.Grade;
import com.wendell.backend.modules.grade.dto.GradeEvaluationResponseDto;
import com.wendell.backend.modules.grade.dto.GradeAuditResponseDto;
import com.wendell.backend.modules.grade.dto.GradeListingResponseDto;
import com.wendell.backend.modules.grade.dto.StudentGradeResponseDto;
import com.wendell.backend.modules.grade.dto.UpdateGradeItemRequestDto;
import com.wendell.backend.modules.grade.dto.UpdateGradesBatchRequestDto;
import com.wendell.backend.modules.grade.repository.GradeAuditRepository;
import com.wendell.backend.modules.grade.repository.GradeRepository;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private GradeAuditRepository gradeAuditRepository;

    @Autowired
    private AuthenticatedUserValidator authenticatedUserValidator;

    public List<StudentGradeResponseDto> listGrades(Long classroomId, Long disciplineId) {
        if (classroomId != null) {
            authenticatedUserValidator.validateClassroomId(classroomId);
        }

        if (disciplineId != null) {
            authenticatedUserValidator.validateDisciplineId(disciplineId);
        }

        List<GradeListingResponseDto> flatGrades = gradeRepository.findAllByClassroomIdAndDisciplineId(classroomId, disciplineId);
        Map<Long, StudentGradeResponseDto> grouped = new LinkedHashMap<>();

        for (GradeListingResponseDto flat : flatGrades) {
            Long studentId = flat.studentId();

            grouped.computeIfAbsent(studentId, id -> new StudentGradeResponseDto(
                    id,
                    flat.studentName(),
                    flat.classroomId(),
                    flat.classroomName(),
                    new ArrayList<>()));

            StudentGradeResponseDto studentDto = grouped.get(studentId);
            studentDto.evaluations().add(new GradeEvaluationResponseDto(
                    flat.gradeId(),
                    flat.evaluationId(),
                    flat.evaluationName(),
                    flat.evaluationWeight(),
                    flat.disciplineId(),
                    flat.disciplineName(),
                    flat.gradeValue()));
        }

        return new ArrayList<>(grouped.values());
    }

    public List<GradeAuditResponseDto> listGradeAuditByStudentAndDiscipline(Long studentId, Long disciplineId) {
        authenticatedUserValidator.validateDisciplineId(disciplineId);

        Long classroomId = gradeRepository.findClassroomIdByStudentId(studentId)
                .orElseThrow(() -> new BadRequestException("Aluno nao encontrado"));

        authenticatedUserValidator.validateClassroomId(classroomId);

        return gradeAuditRepository.findByStudentIdAndDisciplineId(studentId, disciplineId);
    }

    @Transactional
    public void updateGradesBatch(UpdateGradesBatchRequestDto request) {
        Map<Long, BigDecimal> valuesByGradeId = new HashMap<>();

        for (UpdateGradeItemRequestDto item : request.grades()) {
            if (valuesByGradeId.containsKey(item.gradeId())) {
                throw new BadRequestException("Nao e permitido enviar a mesma nota mais de uma vez no mesmo lote");
            }
            valuesByGradeId.put(item.gradeId(), item.gradeValue());
        }

        List<Long> gradeIds = new ArrayList<>(valuesByGradeId.keySet());
        List<Grade> grades = gradeRepository.findAllById(gradeIds);

        if (grades.size() != gradeIds.size()) {
            throw new BadRequestException("Foram informadas notas inexistentes para atualizacao");
        }

        Set<Long> classroomIds = new HashSet<>();
        Set<Long> disciplineIds = new HashSet<>();

        for (Grade grade : grades) {
            classroomIds.add(grade.getStudent().getClassroom().getId());
            disciplineIds.add(grade.getEvaluation().getDiscipline().getId());

            BigDecimal value = valuesByGradeId.get(grade.getId());
            grade.setGrade_value(value);
        }

        for (Long classroomId : classroomIds) {
            authenticatedUserValidator.validateClassroomId(classroomId);
        }

        for (Long disciplineId : disciplineIds) {
            authenticatedUserValidator.validateDisciplineId(disciplineId);
        }

        gradeRepository.saveAll(grades);
    }
}
