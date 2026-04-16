package com.wendell.backend.modules.grade.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Avaliacao do aluno com peso e nota")
public record GradeEvaluationResponseDto(
        @Schema(description = "ID da nota", example = "1")
        Long gradeId,
        @Schema(description = "ID da avaliacao", example = "1")
        Long evaluationId,
        @Schema(description = "Nome da avaliacao", example = "Test 1")
        String evaluationName,
        @Schema(description = "Peso da avaliacao", example = "2.00")
        BigDecimal evaluationWeight,
        @Schema(description = "ID da disciplina", example = "1")
        Long disciplineId,
        @Schema(description = "Nome da disciplina", example = "Matematica")
        String disciplineName,
        @Schema(description = "Nota do aluno", example = "7.5")
        BigDecimal gradeValue
) {
}
