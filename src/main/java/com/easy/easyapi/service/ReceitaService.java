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

    // Busca todas as receitas de um determinado usuário
    public List<Receita> buscarPorUsuario(Usuario usuario) {
        return receitaRepository.findByUsuario(usuario);
    }

    // Salva uma nova receita vinculando-a ao usuário logado
    public Receita salvar(Receita receita, Usuario usuario) {
        receita.setUsuario(usuario);
        return receitaRepository.save(receita);
    }

    // Atualiza os dados de uma receita existente, garantindo que pertença ao usuário
    public Receita atualizar(Long id, Receita receitaAtualizada, Usuario usuario) {
        Receita receita = receitaRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada"));
        receita.setDescricao(receitaAtualizada.getDescricao());
        receita.setValor(receitaAtualizada.getValor());
        receita.setData(receitaAtualizada.getData());
        return receitaRepository.save(receita);
    }

    // Remove uma receita do banco de dados
    public void deletar(Long id, Usuario usuario) {
        Receita receita = receitaRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada"));
        receitaRepository.delete(receita);
    }

    // Busca uma receita específica por ID e Usuário (validação de propriedade)
    public Optional<Receita> buscarPorIdEUsuario(Long id, Usuario usuario) {
        return receitaRepository.findByIdAndUsuario(id, usuario);
    }
}
