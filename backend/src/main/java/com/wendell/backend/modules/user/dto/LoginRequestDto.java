package com.wendell.backend.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados de entrada para autenticação do usuário")
public record LoginRequestDto(
    @Schema(description = "Nome de usuário para login", example = "admin", required = true)
    @NotBlank(message = "Username é obrigatório")
    String username,

    @Schema(description = "Senha do usuário", example = "123", required = true)
    @NotBlank(message = "Password é obrigatório")
    String password
) {}
