package com.easy.easyapi.controller;

import com.easy.easyapi.dto.DespesaCreateDTO;
import com.easy.easyapi.dto.DespesaDTO;
import com.easy.easyapi.model.Despesa;
import com.easy.easyapi.model.Usuario;
import com.easy.easyapi.service.DespesaService;
import com.easy.easyapi.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios/{usuarioId}/despesas")
public class DespesaController {

    private final DespesaService despesaService;
    private final UsuarioService usuarioService;

    public DespesaController(DespesaService despesaService, UsuarioService usuarioService) {
        this.despesaService = despesaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<DespesaDTO>> listar(@PathVariable Long usuarioId) {
        Optional<Usuario> uOpt = usuarioService.buscarPorId(usuarioId);
        if (uOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<DespesaDTO> dtos = despesaService.listarPorUsuario(uOpt.get()).stream()
                .map(DespesaDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<DespesaDTO> criar(@PathVariable Long usuarioId, @Valid @RequestBody DespesaCreateDTO dDto) {
        Optional<Usuario> uOpt = usuarioService.buscarPorId(usuarioId);
        if (uOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Usuario u = uOpt.get();
        Despesa d = dDto.toEntity();
        d.setUsuario(u);
        Despesa salvo = despesaService.salvar(d);
        return ResponseEntity.status(201).body(DespesaDTO.fromEntity(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        despesaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
