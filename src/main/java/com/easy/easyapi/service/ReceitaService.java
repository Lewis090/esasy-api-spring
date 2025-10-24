package com.easy.easyapi.service;

import com.easy.easyapi.model.Receita;
import com.easy.easyapi.model.Usuario;
import com.easy.easyapi.repository.ReceitaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReceitaService {

    private final ReceitaRepository receitaRepository;

    public ReceitaService(ReceitaRepository receitaRepository) {
        this.receitaRepository = receitaRepository;
    }

    public List<Receita> buscarPorUsuario(Usuario usuario) {
        return receitaRepository.findByUsuario(usuario);
    }

    public Receita salvar(Receita receita, Usuario usuario) {
        receita.setUsuario(usuario);
        return receitaRepository.save(receita);
    }

    public Receita atualizar(Long id, Receita receitaAtualizada, Usuario usuario) {
        Receita receita = receitaRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada"));
        receita.setDescricao(receitaAtualizada.getDescricao());
        receita.setValor(receitaAtualizada.getValor());
        receita.setData(receitaAtualizada.getData());
        return receitaRepository.save(receita);
    }

    public void deletar(Long id, Usuario usuario) {
        Receita receita = receitaRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada"));
        receitaRepository.delete(receita);
    }

    public Optional<Receita> buscarPorIdEUsuario(Long id, Usuario usuario) {
        return receitaRepository.findByIdAndUsuario(id, usuario);
    }
}
