package com.wendell.backend.modules.classroom.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de uma sala de aula associada ao usuário")
public record UserClassRoomListResponseDto(
    @Schema(description = "ID único da sala de aula", example = "1")
    Long id,

    @Schema(description = "Nome da sala de aula", example = "Classe A")
    String name
) {}
