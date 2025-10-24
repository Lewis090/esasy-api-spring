package com.easy.easyapi.controller;

import com.easy.easyapi.model.Usuario;
import com.easy.easyapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:8080") // seu front-end
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 🔹 Listar todos usuários
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return ResponseEntity.ok(usuarios);
    }

    // 🔹 Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        return usuarioOpt
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Criar usuário (cadastro)
    @PostMapping
    public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario usuario) {
        // ⚠️ opcional: você pode validar se o email já existe aqui
        Usuario salvo = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // 🔹 Atualizar usuário
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario u) {
        return usuarioRepository.findById(id).map(usuarioExistente -> {
            usuarioExistente.setNome(u.getNome());
            usuarioExistente.setEmail(u.getEmail());
            usuarioExistente.setSenha(u.getSenha());
            Usuario atualizado = usuarioRepository.save(usuarioExistente);
            return ResponseEntity.ok(atualizado);
        }).orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Deletar usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 🔹 Login do usuário
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody Usuario u) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailAndSenha(u.getEmail(), u.getSenha());
        return usuarioOpt
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
