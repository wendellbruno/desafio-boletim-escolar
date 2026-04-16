package com.wendell.backend.modules.grade.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Retorna notas por aluno, disciplina e sala")
public record GradeListingResponseDto(
        @Schema(description = "ID da nota", example = "1")
        Long gradeId,
        @Schema(description = "ID do aluno", example = "1")
        Long studentId,
        @Schema(description = "Nome do aluno", example = "Pedro")
        String studentName,
        @Schema(description = "ID da sala de aula do aluno", example = "1")
        Long classroomId,
        @Schema(description = "Nome da sala de aula do aluno", example = "Classe A")
        String classroomName,
        @Schema(description = "ID da disciplina", example = "1")
        Long disciplineId,
        @Schema(description = "Nome da disciplina", example = "Matematica")
        String disciplineName,
        @Schema(description = "ID da avaliação", example = "1")
        Long evaluationId,
        @Schema(description = "Nome da avaliação", example = "Test 1")
        String evaluationName,
        @Schema(description = "Peso da avaliação", example = "2.00")
        BigDecimal evaluationWeight,
        @Schema(description = "Valor da nota", example = "7.5")
        BigDecimal gradeValue
) {
}
