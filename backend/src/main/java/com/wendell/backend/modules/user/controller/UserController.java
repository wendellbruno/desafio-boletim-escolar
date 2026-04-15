package com.wendell.backend.modules.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wendell.backend.modules.user.dto.LoginRequestDto;
import com.wendell.backend.modules.user.dto.LoginResponseDto;
import com.wendell.backend.modules.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("user")
@Tag(name = "Usuarios", description = "APIs para gerenciamento de usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Operation(
            summary = "Realizar login",
            description = "Autentica um usuario e retorna um token JWT com suas permissoes",
            security = @SecurityRequirement(name = ""))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = LoginResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada invalidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Credenciais invalidas", content = @Content)
    })
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        LoginResponseDto response = userService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
