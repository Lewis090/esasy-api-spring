package com.easy.easyapi.service;

import com.easy.easyapi.model.Usuario;
import com.easy.easyapi.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Salva ou atualiza usuário
    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Busca usuário por ID
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // Busca usuário por email
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Lista todos os usuários
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    // Remove usuário
    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }


}
