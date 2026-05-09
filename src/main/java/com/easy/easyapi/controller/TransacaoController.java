package com.easy.easyapi.controller;

import com.easy.easyapi.dto.TransacaoCreateDTO;
import com.easy.easyapi.model.Despesa;
import com.easy.easyapi.model.Receita;
import com.easy.easyapi.model.Usuario;
import com.easy.easyapi.service.DespesaService;
import com.easy.easyapi.service.ReceitaService;
import com.easy.easyapi.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final ReceitaService receitaService;
    private final DespesaService despesaService;
    private final UsuarioService usuarioService;

    public TransacaoController(ReceitaService receitaService, DespesaService despesaService, UsuarioService usuarioService) {
        this.receitaService = receitaService;
        this.despesaService = despesaService;
        this.usuarioService = usuarioService;
    }

    private boolean isUsuarioAutenticadoOwner(Long usuarioId) {
        Usuario autenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return autenticado.getId().equals(usuarioId);
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listar(@RequestParam Long userId) {
        if (!isUsuarioAutenticadoOwner(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(userId);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Usuario usuario = usuarioOpt.get();

        List<Map<String, Object>> receitas = receitaService.buscarPorUsuario(usuario).stream().map(r -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", r.getId());
            m.put("descricao", r.getDescricao());
            m.put("valor", r.getValor());
            m.put("data", r.getData() != null ? r.getData().toString() : null);
            m.put("tipo", "RECEITA");
            return m;
        }).collect(Collectors.toList());

        List<Map<String, Object>> despesas = despesaService.listarPorUsuario(usuario).stream().map(d -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", d.getId());
            m.put("descricao", d.getDescricao());
            m.put("valor", d.getValor());
            m.put("data", d.getData() != null ? d.getData().toString() : null);
            m.put("tipo", "DESPESA_VARIAVEL");
            return m;
        }).collect(Collectors.toList());

        List<Map<String, Object>> todas = Stream.concat(receitas.stream(), despesas.stream())
                .sorted((a, b) -> String.valueOf(b.get("data")).compareTo(String.valueOf(a.get("data"))))
                .collect(Collectors.toList());

        return ResponseEntity.ok(todas);
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestParam Long userId, @Valid @RequestBody TransacaoCreateDTO dto) {
        if (!isUsuarioAutenticadoOwner(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(userId);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = usuarioOpt.get();
        String tipo = dto.getTipo() != null ? dto.getTipo().toUpperCase() : "";

        if ("RECEITA".equals(tipo) || ("".equals(tipo) && dto.getValor() >= 0)) {
            Receita receita = new Receita();
            receita.setDescricao(dto.getDescricao());
            receita.setValor(Math.abs(dto.getValor()));
            receita.setData(dto.getData());
            Receita novaReceita = receitaService.salvar(receita, usuario);
            return new ResponseEntity<>(novaReceita, HttpStatus.CREATED);
        } else if ("DESPESA".equals(tipo) || tipo.startsWith("DESPESA") || ("".equals(tipo) && dto.getValor() < 0)) {
            Despesa despesa = new Despesa();
            despesa.setDescricao(dto.getDescricao());
            despesa.setValor(Math.abs(dto.getValor()));
            despesa.setData(dto.getData());
            despesa.setUsuario(usuario);
            Despesa novaDespesa = despesaService.salvar(despesa);
            return new ResponseEntity<>(novaDespesa, HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().body("Tipo de transação inválido ou não foi possível inferir pelo valor.");
        }
    }
}
