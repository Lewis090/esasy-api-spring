package com.easy.easyapi.dto;

import com.easy.easyapi.model.Receita;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReceitaDTO {
    private Long id;
    private String descricao;
    private Double valor;
    private LocalDateTime data;
    private Long usuarioId;

    public static ReceitaDTO fromEntity(Receita r) {
        if (r == null) return null;
        ReceitaDTO dto = new ReceitaDTO();
        dto.setId(r.getId());
        dto.setDescricao(r.getDescricao());
        dto.setValor(r.getValor());
        dto.setData(r.getData());
        dto.setUsuarioId(r.getUsuario() != null ? r.getUsuario().getId() : null);
        return dto;
    }
}

