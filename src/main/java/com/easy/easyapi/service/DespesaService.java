package com.easy.easyapi.service;

import com.easy.easyapi.model.Despesa;
import com.easy.easyapi.repository.DespesaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DespesaService {
    private final DespesaRepository repo;

    public DespesaService(DespesaRepository repo) {
        this.repo = repo;
    }

    public List<Despesa> listar() {
        return repo.findAll();
    }

    public Despesa salvar(Despesa d) {
        return repo.save(d);
    }

    public Despesa atualizar(Long id, Despesa d) {
        Despesa existente = repo.findById(id).orElseThrow(() -> new RuntimeException("Despesa não encontrada"));
        if (d.getDescricao() != null) existente.setDescricao(d.getDescricao());
        if (d.getValor() != null) existente.setValor(d.getValor());
        if (d.getData() != null) existente.setData(d.getData());
        return repo.save(existente);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}
