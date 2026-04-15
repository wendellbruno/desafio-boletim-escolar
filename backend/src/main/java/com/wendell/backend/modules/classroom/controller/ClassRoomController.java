package com.wendell.backend.modules.classroom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wendell.backend.modules.classroom.dto.UserClassRoomListResponseDto;
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

    @GetMapping("/user/{userId}")
    @Operation(summary = "Listar salas de aula do usuário",
               description = "Retorna todas as salas de aula associadas a um usuário específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Salas de aula encontradas",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserClassRoomListResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
            content = @Content)
    })
    public List<UserClassRoomListResponseDto> listAvailableClassrooms(
            @Parameter(description = "ID do usuário", required = true, example = "1")
            @PathVariable Long userId) {
        return classRoomService.listAvailableClassroomsByUserId(userId);
    }
}
