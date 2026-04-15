package com.wendell.backend.modules.grade.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Notas agrupadas por aluno")
public record StudentGradeResponseDto(
        @Schema(description = "ID do aluno", example = "1")
        Long studentId,
        @Schema(description = "Nome do aluno", example = "Pedro")
        String studentName,
        @Schema(description = "ID da sala", example = "1")
        Long classroomId,
        @Schema(description = "Nome da sala", example = "Classe A")
        String classroomName,
        @Schema(description = "Lista de avaliações e notas do aluno")
        List<GradeEvaluationResponseDto> evaluations
) {
}
