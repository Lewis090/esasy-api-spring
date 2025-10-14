package com.easy.easyapi.service;

import com.easy.easyapi.model.Receita;
import com.easy.easyapi.repository.ReceitaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceitaService {
    private final ReceitaRepository repo;

    public ReceitaService(ReceitaRepository repo) {
        this.repo = repo;
    }

    public List<Receita> listar() {
        return repo.findAll();
    }

    public Receita salvar(Receita r) {
        return repo.save(r);
    }

    public Receita atualizar(Long id, Receita r) {
        Receita existente = repo.findById(id).orElseThrow(() -> new RuntimeException("Receita não encontrada"));
        if (r.getDescricao() != null) existente.setDescricao(r.getDescricao());
        if (r.getValor() != null) existente.setValor(r.getValor());
        if (r.getData() != null) existente.setData(r.getData());
        return repo.save(existente);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}
