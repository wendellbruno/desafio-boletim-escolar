package com.wendell.backend.modules.grade.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wendell.backend.modules.grade.dto.GradeAuditResponseDto;
import com.wendell.backend.modules.grade.dto.StudentGradeResponseDto;
import com.wendell.backend.modules.grade.dto.UpdateGradesBatchRequestDto;
import com.wendell.backend.modules.grade.service.GradeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/grades")
@Tag(name = "Notas", description = "APIs para consulta e atualizacao de notas")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @GetMapping
    @Operation(summary = "Listar notas por aluno",
            description = "Retorna notas agrupadas por aluno, com avaliacoes, peso e valor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notas encontradas", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StudentGradeResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Nao autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public List<StudentGradeResponseDto> listGrades(
            @Parameter(description = "ID da sala para filtrar", example = "1")
            @RequestParam(required = false) Long classroomId,
            @Parameter(description = "ID da disciplina para filtrar", example = "1")
            @RequestParam(required = false) Long disciplineId) {
        return gradeService.listGrades(classroomId, disciplineId);
    }

    @GetMapping("/audit")
    @Operation(summary = "Listar auditoria de notas por aluno",
            description = "Retorna o historico de alteracoes das notas por aluno e disciplina")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historico encontrado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GradeAuditResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Nao autorizado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content)
    })
    public List<GradeAuditResponseDto> listGradeAudit(
            @Parameter(description = "ID do aluno", required = true, example = "1")
            @RequestParam Long studentId,
            @Parameter(description = "ID da disciplina", required = true, example = "1")
            @RequestParam Long disciplineId) {
        return gradeService.listGradeAuditByStudentAndDiscipline(studentId, disciplineId);
    }

    @PutMapping
    @Operation(summary = "Atualizar notas em lote",
            description = "Atualiza varias notas em uma unica requisicao")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notas atualizadas com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Nao autorizado", content = @Content)
    })
    public ResponseEntity<Void> updateGradesBatch(@Valid @RequestBody UpdateGradesBatchRequestDto request) {
        gradeService.updateGradesBatch(request);
        return ResponseEntity.noContent().build();
    }
}
