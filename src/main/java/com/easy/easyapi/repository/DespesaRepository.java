package com.easy.easyapi.repository;

import com.easy.easyapi.model.Despesa;
import com.easy.easyapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {
    List<Despesa> findByUsuario(Usuario usuario);
}
