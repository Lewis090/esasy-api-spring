package com.easy.easyapi.dto;

import com.easy.easyapi.model.Receita;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReceitaCreateDTO {
    @NotBlank
    private String descricao;

    @NotNull
    private Double valor;

    @NotNull
    private LocalDate data;

    public Receita toEntity() {
        Receita r = new Receita();
        r.setDescricao(this.descricao);
        r.setValor(this.valor);
        r.setData(this.data);
        return r;
    }
}
