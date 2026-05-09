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
import java.util.Optional;

@RestController
@RequestMapping("/transacao")
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
        } else if ("DESPESA".equals(tipo) || ("".equals(tipo) && dto.getValor() < 0)) {
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
