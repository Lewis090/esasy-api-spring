package com.easy.easyapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransacaoCreateDTO {
    @NotBlank
    private String descricao;

    @NotNull
    private Double valor;

    @NotNull
    private LocalDateTime data;

    // Tipo opcional ("RECEITA" ou "DESPESA")
    private String tipo;
}
