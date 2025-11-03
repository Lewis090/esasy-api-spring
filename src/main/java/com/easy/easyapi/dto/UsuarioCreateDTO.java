package com.easy.easyapi.dto;

import com.easy.easyapi.model.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioCreateDTO {
    @NotBlank
    private String nome;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6)
    private String senha;

    public Usuario toEntity() {
        Usuario u = new Usuario();
        u.setNome(this.nome);
        u.setEmail(this.email);
        u.setSenha(this.senha);
        return u;
    }
}
