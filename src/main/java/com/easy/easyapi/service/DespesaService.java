package com.easy.easyapi.service;

import com.easy.easyapi.model.Despesa;
import com.easy.easyapi.model.Usuario;
import com.easy.easyapi.repository.DespesaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DespesaService {

    private final DespesaRepository repo;

    public DespesaService(DespesaRepository repo) {
        this.repo = repo;
    }

    public List<Despesa> listarPorUsuario(Usuario u) {
        return repo.findByUsuario(u);
    }

    public Despesa salvar(Despesa d) {
        return repo.save(d);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}
