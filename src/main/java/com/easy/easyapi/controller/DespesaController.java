package com.easy.easyapi.controller;

import com.easy.easyapi.model.Despesa;
import com.easy.easyapi.service.DespesaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/despesas")
@CrossOrigin(origins = "*")
public class DespesaController {

    private final DespesaService service;

    public DespesaController(DespesaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Despesa> listar() {
         return service.listar();
    }

    @PostMapping
    public ResponseEntity<String> criar(@RequestBody Despesa d) {
        service.salvar(d);
        String mensagem = "Despesa criada com sucesso!! =)";
        return  new ResponseEntity<>(mensagem, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizar(@PathVariable Long id, @RequestBody Despesa d) {
         service.atualizar(id, d);
         String mensagem = "Despesa Atualizada!";
         return new ResponseEntity<>(mensagem,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
