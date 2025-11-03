package com.easy.easyapi.controller;

import com.easy.easyapi.dto.UsuarioCreateDTO;
import com.easy.easyapi.model.Usuario;
import com.easy.easyapi.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
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
    public ResponseEntity<Map<String, Object>> cadastrar(@Valid @RequestBody UsuarioCreateDTO usuarioDto) {
        Map<String, Object> response = new HashMap<>();

        // Verifica se email já existe
        if (usuarioService.buscarPorEmail(usuarioDto.getEmail()).isPresent()) {
            response.put("mensagem", "Email já cadastrado");
            return ResponseEntity.badRequest().body(response);
        }

        // Salva usuário
        Usuario salvo = usuarioService.salvar(usuarioDto.toEntity());
        response.put("mensagem", "Usuário cadastrado com sucesso!");
        response.put("id", salvo.getId());
        response.put("nome", salvo.getNome());

        return ResponseEntity.ok(response);
    }

    // --- Login ---
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Map<String, Object>> login(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String senha,
            @RequestBody(required = false) UsuarioCreateDTO usuarioDto) {
        Map<String, Object> response = new HashMap<>();

        // Prioriza dados do body, senão usa query params
        String emailFinal = (usuarioDto != null && usuarioDto.getEmail() != null) ? usuarioDto.getEmail() : email;
        String senhaFinal = (usuarioDto != null && usuarioDto.getSenha() != null) ? usuarioDto.getSenha() : senha;

        if (emailFinal == null || senhaFinal == null) {
            response.put("mensagem", "Email e senha são obrigatórios");
            return ResponseEntity.badRequest().body(response);
        }

        return usuarioService.buscarPorEmail(emailFinal)
                .map(u -> {
                    if (u.getSenha().equals(senhaFinal)) {
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
