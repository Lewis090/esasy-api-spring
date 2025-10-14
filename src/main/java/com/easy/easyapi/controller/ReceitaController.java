package com.easy.easyapi.controller;

import com.easy.easyapi.model.Receita;
import com.easy.easyapi.service.ReceitaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/receitas")
@CrossOrigin(origins = "*")
public class ReceitaController {

    private final ReceitaService service;

    public ReceitaController(ReceitaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Receita> listar() {
        return service.listar();
    }

    @PostMapping
    public Receita criar(@RequestBody Receita r) {
        return service.salvar(r);
    }

    @PutMapping("/{id}")
    public Receita atualizar(@PathVariable Long id, @RequestBody Receita r) {
        return service.atualizar(id, r);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
