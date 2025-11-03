package com.easy.easyapi.dto;

import com.easy.easyapi.model.Despesa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DespesaCreateDTO {
    @NotBlank
    private String descricao;

    @NotNull
    private Double valor;

    @NotNull
    private LocalDateTime data;

    public Despesa toEntity() {
        Despesa d = new Despesa();
        d.setDescricao(this.descricao);
        d.setValor(this.valor);
        d.setData(this.data);
        return d;
    }
}
