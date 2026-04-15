package com.wendell.backend.modules.grade.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wendell.backend.modules.grade.dto.GradeEvaluationResponseDto;
import com.wendell.backend.modules.grade.dto.GradeListingResponseDto;
import com.wendell.backend.modules.grade.dto.StudentGradeResponseDto;
import com.wendell.backend.modules.grade.repository.GradeRepository;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    public List<StudentGradeResponseDto> listGrades(Long classroomId, Long disciplineId) {
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
                    flat.evaluationId(),
                    flat.evaluationName(),
                    flat.evaluationWeight(),
                    flat.disciplineId(),
                    flat.disciplineName(),
                    flat.gradeValue()));
        }

        return new ArrayList<>(grouped.values());
    }
}
