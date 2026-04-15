package com.wendell.backend.modules.classroom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wendell.backend.modules.classroom.dto.UserClassRoomListResponseDto;
import com.wendell.backend.modules.classroom.dto.UserDisciplineClassRoomListResponseDto;
import com.wendell.backend.modules.classroom.service.ClassRoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/classrooms")
@Tag(name = "Salas de Aula", description = "APIs para gerenciamento de salas de aula")
public class ClassRoomController {

    @Autowired
    private ClassRoomService classRoomService;

    @GetMapping("/user")
    @Operation(summary = "Listar salas de aula do usuario",
            description = "Retorna todas as salas de aula associadas a um usuario especifico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Salas de aula encontradas", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserClassRoomListResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario nao encontrado", content = @Content)
    })
    public List<UserClassRoomListResponseDto> listAvailableClassrooms(
            @Parameter(description = "ID do usuario", required = true, example = "1")
            @RequestParam Long userId) {
        return classRoomService.listAvailableClassroomsByUserId(userId);
    }

    @GetMapping("/disciplines")
    @Operation(summary = "Listar disciplinas da sala para o usuario",
            description = "Retorna as disciplinas ativas associadas a um usuario em uma sala especifica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disciplinas encontradas", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDisciplineClassRoomListResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario ou sala nao encontrados", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario nao possui acesso a esta sala ", content = @Content)
    })
    public List<UserDisciplineClassRoomListResponseDto> listAvailableDisciplinesClassrooms(
            @Parameter(description = "ID do usuario", required = true, example = "1")
            @RequestParam Long userId,
            @Parameter(description = "ID da sala", required = true, example = "1")
            @RequestParam Long classroomId) {
        return classRoomService.listAvailableDisciplineByClassIDAndByUserId(classroomId, userId);
    }
}
