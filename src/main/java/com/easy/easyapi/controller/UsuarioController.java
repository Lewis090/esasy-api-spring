package com.easy.easyapi.controller;

import com.easy.easyapi.model.Usuario;
import com.easy.easyapi.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listar() {
        List<Usuario> usuarios = service.listar();

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("mensagem", "Lista de usuários carregada com sucesso!");
        resposta.put("usuarios", usuarios);

        return ResponseEntity.ok(resposta);
    }

    @PostMapping

    public ResponseEntity<Map<String, Object>> criar(@RequestBody Usuario u) {
        Usuario novoUsuario = service.salvar(u);
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("mesagem:", "Usuario criado com sucesso!");
        resposta.put("Usuario", novoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizar(@PathVariable Long id, @RequestBody Usuario u) {
        Map<String, Object> resposta = new HashMap<>();

        try {
            Usuario usuarioAtualizado = service.atualizar(id, u);
            resposta.put("mensagem", "Usuário atualizado com sucesso!");
            resposta.put("usuario", usuarioAtualizado);
            return ResponseEntity.ok(resposta);
        } catch (RuntimeException e) {
            resposta.put("mensagem", "Erro ao atualizar usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletar(@PathVariable Long id) {
        Map<String, Object> resposta = new HashMap<>();

        try {
            service.deletar(id);
            resposta.put("mensagem", "Usuário deletado com sucesso!");
            resposta.put("id", id);
            return ResponseEntity.ok(resposta);
        } catch (RuntimeException e) {
            resposta.put("mensagem", "Erro ao deletar usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
        }
    }
}