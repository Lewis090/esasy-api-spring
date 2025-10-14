package com.easy.easyapi.service;

import com.easy.easyapi.model.Usuario;
import com.easy.easyapi.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    public List<Usuario> listar() {
        return repo.findAll();
    }

    public Usuario salvar(Usuario u) {
        return repo.save(u);
    }

    public Usuario atualizar(Long id, Usuario u) {
        Usuario existente = repo.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if (u.getNome() != null) existente.setNome(u.getNome());
        if (u.getEmail() != null) existente.setEmail(u.getEmail());
        return repo.save(existente);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}
