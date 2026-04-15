
package com.wendell.backend.modules.grade.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description =  "Listar alunos e notas das avaliações por turma/Disciplina")
record ListGradesEvolutionsResponseDTO(

    @Schema(description = "ID único do aluno", example = "1")
    Long   studentId,

    @Schema(description = "Nome do aluno", example = "Wendell")
    String studentName,

    @Schema(description = "ID único da avaliação", example = "1")
    Long   evolutionId,

    @Schema(description = "Nome da avaliação", example = "Projeto")
    String evolutionName,

    @Schema(description = "Peso da avaliação", example = "1.50")
    BigDecimal evalutionWeight
    

) {
}