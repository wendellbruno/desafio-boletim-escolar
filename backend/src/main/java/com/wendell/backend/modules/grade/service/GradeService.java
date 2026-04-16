package com.wendell.backend.modules.grade.service;

import java.util.ArrayList;
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
import com.wendell.backend.modules.grade.repository.EvaluationLookupRepository;
import com.wendell.backend.modules.grade.repository.GradeAuditRepository;
import com.wendell.backend.modules.grade.repository.GradeRepository;
import com.wendell.backend.modules.grade.repository.StudentLookupRepository;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private GradeAuditRepository gradeAuditRepository;

    @Autowired
    private StudentLookupRepository studentLookupRepository;

    @Autowired
    private EvaluationLookupRepository evaluationLookupRepository;

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
        Set<Long> processedGradeIds = new HashSet<>();
        Set<String> processedStudentEvaluationPairs = new HashSet<>();
        List<Grade> gradesToSave = new ArrayList<>();

        Set<Long> classroomIds = new HashSet<>();
        Set<Long> disciplineIds = new HashSet<>();

        for (UpdateGradeItemRequestDto item : request.grades()) {
            Grade grade;

            if (item.gradeId() != null) {
                if (!processedGradeIds.add(item.gradeId())) {
                    throw new BadRequestException("Nao e permitido enviar a mesma nota mais de uma vez no mesmo lote");
                }

                grade = gradeRepository.findById(item.gradeId())
                        .orElseThrow(() -> new BadRequestException("Foram informadas notas inexistentes para atualizacao"));
            } else {
                if (item.studentId() == null || item.evaluationId() == null) {
                    throw new BadRequestException("Quando gradeId nao for informado, studentId e evaluationId sao obrigatorios");
                }

                String pairKey = item.studentId() + "-" + item.evaluationId();
                if (!processedStudentEvaluationPairs.add(pairKey)) {
                    throw new BadRequestException("Nao e permitido enviar o mesmo aluno/avaliacao mais de uma vez no mesmo lote");
                }

                grade = gradeRepository.findByStudentIdAndEvaluationId(item.studentId(), item.evaluationId()).orElse(null);

                if (grade == null) {
                    if (!studentLookupRepository.existsById(item.studentId())) {
                        throw new BadRequestException("Aluno nao encontrado para lancamento de nota");
                    }
                    if (!evaluationLookupRepository.existsById(item.evaluationId())) {
                        throw new BadRequestException("Avaliacao nao encontrada para lancamento de nota");
                    }

                    grade = new Grade();
                    grade.setStudent(studentLookupRepository.getReferenceById(item.studentId()));
                    grade.setEvaluation(evaluationLookupRepository.getReferenceById(item.evaluationId()));
                }
            }

            Long classroomId = grade.getStudent().getClassroom().getId();
            Long disciplineId = grade.getEvaluation().getDiscipline().getId();

            classroomIds.add(classroomId);
            disciplineIds.add(disciplineId);
            grade.setGrade_value(item.gradeValue());
            gradesToSave.add(grade);
        }

        for (Long classroomId : classroomIds) {
            authenticatedUserValidator.validateClassroomId(classroomId);
        }

        for (Long disciplineId : disciplineIds) {
            authenticatedUserValidator.validateDisciplineId(disciplineId);
        }

        gradeRepository.saveAll(gradesToSave);
    }
}
