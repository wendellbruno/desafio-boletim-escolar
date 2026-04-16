package com.wendell.backend.modules.grade.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record UpdateGradesBatchRequestDto(
        @NotEmpty(message = "A lista de notas nao pode ser vazia")
        List<@Valid UpdateGradeItemRequestDto> grades
) {
}
