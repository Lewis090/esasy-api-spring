package com.easy.easyapi.controller;

import com.easy.easyapi.dto.ReceitaCreateDTO;
import com.easy.easyapi.dto.ReceitaDTO;
import com.easy.easyapi.model.Receita;
import com.easy.easyapi.model.Usuario;
import com.easy.easyapi.service.ReceitaService;
import com.easy.easyapi.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios/{usuarioId}/receitas")
public class ReceitaController {

    private final ReceitaService receitaService;
    private final UsuarioService usuarioService;

    public ReceitaController(ReceitaService receitaService, UsuarioService usuarioService) {
        this.receitaService = receitaService;
        this.usuarioService = usuarioService;
    }

    private boolean isUsuarioAutenticadoOwner(Long usuarioId) {
        Usuario autenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return autenticado.getId().equals(usuarioId);
    }

    // Listar todas as receitas do usuário
    @GetMapping
    public ResponseEntity<List<ReceitaDTO>> listar(@PathVariable Long usuarioId) {
        if (!isUsuarioAutenticadoOwner(usuarioId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(usuarioId);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<ReceitaDTO> dtos = receitaService.buscarPorUsuario(usuarioOpt.get()).stream()
                .map(ReceitaDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Criar nova receita
    @PostMapping
    public ResponseEntity<ReceitaDTO> criar(@PathVariable Long usuarioId, @Valid @RequestBody ReceitaCreateDTO receitaDto) {
        if (!isUsuarioAutenticadoOwner(usuarioId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(usuarioId);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Receita novaReceita = receitaService.salvar(receitaDto.toEntity(), usuarioOpt.get());
        return new ResponseEntity<>(ReceitaDTO.fromEntity(novaReceita), HttpStatus.CREATED);
    }

    // Atualizar receita
    @PutMapping("/{id}")
    public ResponseEntity<ReceitaDTO> atualizar(@PathVariable Long usuarioId,
                                             @PathVariable Long id,
                                             @Valid @RequestBody ReceitaCreateDTO receitaAtualizada) {
        if (!isUsuarioAutenticadoOwner(usuarioId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(usuarioId);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Receita receita = receitaService.atualizar(id, receitaAtualizada.toEntity(), usuarioOpt.get());
        return ResponseEntity.ok(ReceitaDTO.fromEntity(receita));
    }

    // Deletar receita
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long usuarioId, @PathVariable Long id) {
        if (!isUsuarioAutenticadoOwner(usuarioId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(usuarioId);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        receitaService.deletar(id, usuarioOpt.get());
        return ResponseEntity.noContent().build();
    }

    // Buscar receita específica
    @GetMapping("/{id}")
    public ResponseEntity<ReceitaDTO> buscar(@PathVariable Long usuarioId, @PathVariable Long id) {
        if (!isUsuarioAutenticadoOwner(usuarioId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(usuarioId);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Optional<Receita> receitaOpt = receitaService.buscarPorIdEUsuario(id, usuarioOpt.get());
        if (receitaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ReceitaDTO.fromEntity(receitaOpt.get()));
    }
}
