package com.easy.easyapi.dto;

import com.easy.easyapi.model.Despesa;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DespesaDTO {
    private Long id;
    private String descricao;
    private Double valor;
    private LocalDate data;
    private Long usuarioId;

    public static DespesaDTO fromEntity(Despesa d) {
        if (d == null) return null;
        DespesaDTO dto = new DespesaDTO();
        dto.setId(d.getId());
        dto.setDescricao(d.getDescricao());
        dto.setValor(d.getValor());
        dto.setData(d.getData());
        dto.setUsuarioId(d.getUsuario() != null ? d.getUsuario().getId() : null);
        return dto;
    }
}

