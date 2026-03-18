package com.easy.easyapi.controller;

import com.easy.easyapi.dto.LoginResponse;
import com.easy.easyapi.dto.UsuarioCreateDTO;
import com.easy.easyapi.model.Usuario;
import com.easy.easyapi.security.JwtService;
import com.easy.easyapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // --- Cadastro ---
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> cadastrar(@Valid @RequestBody UsuarioCreateDTO usuarioDto) {
        Map<String, Object> response = new HashMap<>();

        // Verifica se email já existe
        if (usuarioService.buscarPorEmail(usuarioDto.getEmail()).isPresent()) {
            response.put("mensagem", "Email já cadastrado");
            return ResponseEntity.badRequest().body(response);
        }

        // Salva usuário (o serviço agora criptografa a senha)
        Usuario salvo = usuarioService.salvar(usuarioDto.toEntity());
        
        String jwtToken = jwtService.generateToken(salvo);

        response.put("mensagem", "Usuário cadastrado com sucesso!");
        response.put("token", jwtToken);
        response.put("id", salvo.getId());
        response.put("nome", salvo.getNome());

        return ResponseEntity.ok(response);
    }

    // --- Login ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioCreateDTO usuarioDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        usuarioDto.getEmail(),
                        usuarioDto.getSenha()
                )
        );

        Usuario user = (Usuario) authentication.getPrincipal();
        String jwtToken = jwtService.generateToken(user);

        return ResponseEntity.ok(LoginResponse.builder()
                .token(jwtToken)
                .id(user.getId())
                .nome(user.getNome())
                .mensagem("Login realizado com sucesso!")
                .build());
    }
}
