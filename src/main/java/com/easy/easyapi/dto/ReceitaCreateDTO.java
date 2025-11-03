package com.easy.easyapi.dto;

import com.easy.easyapi.model.Receita;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReceitaCreateDTO {
    @NotBlank
    private String descricao;

    @NotNull
    private Double valor;

    @NotNull
    private LocalDateTime data;

    public Receita toEntity() {
        Receita r = new Receita();
        r.setDescricao(this.descricao);
        r.setValor(this.valor);
        r.setData(this.data);
        return r;
    }
}
