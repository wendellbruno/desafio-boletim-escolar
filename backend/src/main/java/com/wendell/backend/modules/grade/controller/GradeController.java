package com.wendell.backend.modules.grade.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wendell.backend.modules.grade.dto.StudentGradeResponseDto;
import com.wendell.backend.modules.grade.service.GradeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/grades")
@Tag(name = "Notas", description = "APIs para consulta de notas")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @GetMapping
    @Operation(summary = "Listar notas por aluno",
            description = "Retorna notas agrupadas por aluno, com avaliações, peso e valor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notas encontradas", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StudentGradeResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public List<StudentGradeResponseDto> listGrades(
            @Parameter(description = "ID da sala para filtrar", example = "1")
            @RequestParam(required = false) Long classroomId,
            @Parameter(description = "ID da disciplina para filtrar", example = "1")
            @RequestParam(required = false) Long disciplineId) {
        return gradeService.listGrades(classroomId, disciplineId);
    }
}
