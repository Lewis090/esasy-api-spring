package com.easy.easyapi.controller;

import com.easy.easyapi.dto.UsuarioCreateDTO;
import com.easy.easyapi.dto.UsuarioDTO;
import com.easy.easyapi.model.Usuario;
import com.easy.easyapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:8080") // seu front-end
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 🔹 Listar todos usuários
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioDTO> dtos = usuarios.stream().map(UsuarioDTO::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // 🔹 Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        return usuarioOpt
                .map(u -> ResponseEntity.ok(UsuarioDTO.fromEntity(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Criar usuário (cadastro)
    @PostMapping
    public ResponseEntity<UsuarioDTO> criarUsuario(@Valid @RequestBody UsuarioCreateDTO usuarioDto) {
        Usuario usuario = usuarioDto.toEntity();
        Usuario salvo = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioDTO.fromEntity(salvo));
    }

    // 🔹 Atualizar usuário
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioCreateDTO u) {
        return usuarioRepository.findById(id).map(usuarioExistente -> {
            usuarioExistente.setNome(u.getNome());
            usuarioExistente.setEmail(u.getEmail());
            usuarioExistente.setSenha(u.getSenha());
            Usuario atualizado = usuarioRepository.save(usuarioExistente);
            return ResponseEntity.ok(UsuarioDTO.fromEntity(atualizado));
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
    public ResponseEntity<UsuarioDTO> login(@RequestBody UsuarioCreateDTO u) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailAndSenha(u.getEmail(), u.getSenha());
        return usuarioOpt
                .map(user -> ResponseEntity.ok(UsuarioDTO.fromEntity(user)))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
