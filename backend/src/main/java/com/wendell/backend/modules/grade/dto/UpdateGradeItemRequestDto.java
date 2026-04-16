package com.wendell.backend.modules.grade.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record UpdateGradeItemRequestDto(
        @NotNull(message = "gradeId e obrigatorio")
        Long gradeId,
        @NotNull(message = "gradeValue e obrigatorio")
        @DecimalMin(value = "0.0", message = "A nota nao pode ser menor que 0")
        @DecimalMax(value = "10.0", message = "A nota nao pode ser maior que 10")
        BigDecimal gradeValue
) {
}
