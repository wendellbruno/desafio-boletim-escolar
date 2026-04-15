package com.wendell.backend.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de autenticação contendo dados do usuário e token JWT")
public record LoginResponseDto(
    @Schema(description = "ID único do usuário", example = "1")
    Long id,

    @Schema(description = "Nome de usuário", example = "admin")
    String username,

    @Schema(description = "Token JWT para autenticação", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String token
) {}
