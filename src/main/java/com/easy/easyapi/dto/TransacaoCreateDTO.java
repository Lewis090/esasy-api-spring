package com.easy.easyapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TransacaoCreateDTO {
    @NotBlank
    private String descricao;

    @NotNull
    private Double valor;

    @NotNull
    private LocalDate data;

    // Tipo opcional ("RECEITA" ou "DESPESA" / "DESPESA_VARIAVEL")
    private String tipo;
}
