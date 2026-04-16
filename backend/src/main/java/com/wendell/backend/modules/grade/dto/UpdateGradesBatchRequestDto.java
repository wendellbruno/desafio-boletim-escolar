package com.wendell.backend.modules.grade.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateGradesBatchRequestDto(
        @NotNull(message = "modifiedByUserId e obrigatorio")
        Long modifiedByUserId,
        @NotEmpty(message = "A lista de notas nao pode ser vazia")
        List<@Valid UpdateGradeItemRequestDto> grades
) {
}
