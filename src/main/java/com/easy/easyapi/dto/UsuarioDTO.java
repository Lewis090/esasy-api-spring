package com.easy.easyapi.dto;

import com.easy.easyapi.model.Usuario;
import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;

    public static UsuarioDTO fromEntity(Usuario u) {
        if (u == null) return null;
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(u.getId());
        dto.setNome(u.getNome());
        dto.setEmail(u.getEmail());
        return dto;
    }
}

