package com.easy.easyapi.controller;

import com.easy.easyapi.model.Usuario;
import com.easy.easyapi.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // --- Cadastro ---
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> cadastrar(@RequestBody Usuario usuario) {
        Map<String, Object> response = new HashMap<>();

        // Verifica se email já existe
        if (usuarioService.buscarPorEmail(usuario.getEmail()).isPresent()) {
            response.put("mensagem", "Email já cadastrado");
            return ResponseEntity.badRequest().body(response);
        }

        // Salva usuário
        Usuario salvo = usuarioService.salvar(usuario);
        response.put("mensagem", "Usuário cadastrado com sucesso!");
        response.put("id", salvo.getId());
        response.put("nome", salvo.getNome());

        return ResponseEntity.ok(response);
    }

    // --- Login ---
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Usuario usuario) {
        Map<String, Object> response = new HashMap<>();

        return usuarioService.buscarPorEmail(usuario.getEmail())
                .map(u -> {
                    if (u.getSenha().equals(usuario.getSenha())) {
                        response.put("id", u.getId());
                        response.put("nome", u.getNome());
                        response.put("mensagem", "Login realizado com sucesso!");
                        return ResponseEntity.ok(response);
                    } else {
                        response.put("mensagem", "Senha incorreta");
                        return ResponseEntity.badRequest().body(response);
                    }
                })
                .orElseGet(() -> {
                    response.put("mensagem", "Usuário não encontrado");
                    return ResponseEntity.badRequest().body(response);
                });
    }
}
