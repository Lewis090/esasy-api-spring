package com.easy.easyapi.controller;

import com.easy.easyapi.model.Despesa;
import com.easy.easyapi.model.Usuario;
import com.easy.easyapi.service.DespesaService;
import com.easy.easyapi.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<Despesa> listar(@PathVariable Long usuarioId) {
        Usuario u = usuarioService.buscarPorId(usuarioId).orElseThrow();
        return despesaService.listarPorUsuario(u);
    }

    @PostMapping
    public Despesa criar(@PathVariable Long usuarioId, @RequestBody Despesa d) {
        Usuario u = usuarioService.buscarPorId(usuarioId).orElseThrow();
        d.setUsuario(u);
        return despesaService.salvar(d);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        despesaService.deletar(id);
    }
}
