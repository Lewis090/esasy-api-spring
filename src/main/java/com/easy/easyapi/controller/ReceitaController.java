package com.easy.easyapi.controller;

import com.easy.easyapi.model.Receita;
import com.easy.easyapi.model.Usuario;
import com.easy.easyapi.service.ReceitaService;
import com.easy.easyapi.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios/{usuarioId}/receitas")
public class ReceitaController {

    private final ReceitaService receitaService;
    private final UsuarioService usuarioService;

    public ReceitaController(ReceitaService receitaService, UsuarioService usuarioService) {
        this.receitaService = receitaService;
        this.usuarioService = usuarioService;
    }

    // Listar todas as receitas do usuário
    @GetMapping
    public ResponseEntity<List<Receita>> listar(@PathVariable Long usuarioId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        List<Receita> receitas = receitaService.buscarPorUsuario(usuario);
        return ResponseEntity.ok(receitas);
    }

    // Criar nova receita
    @PostMapping
    public ResponseEntity<Receita> criar(@PathVariable Long usuarioId, @RequestBody Receita receita) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Receita novaReceita = receitaService.salvar(receita, usuario);
        return new ResponseEntity<>(novaReceita, HttpStatus.CREATED);
    }

    // Atualizar receita
    @PutMapping("/{id}")
    public ResponseEntity<Receita> atualizar(@PathVariable Long usuarioId,
                                             @PathVariable Long id,
                                             @RequestBody Receita receitaAtualizada) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Receita receita = receitaService.atualizar(id, receitaAtualizada, usuario);
        return ResponseEntity.ok(receita);
    }

    // Deletar receita
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long usuarioId, @PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        receitaService.deletar(id, usuario);
        return ResponseEntity.noContent().build();
    }

    // Buscar receita específica
    @GetMapping("/{id}")
    public ResponseEntity<Receita> buscar(@PathVariable Long usuarioId, @PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Receita receita = receitaService.buscarPorIdEUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada"));
        return ResponseEntity.ok(receita);
    }
}
