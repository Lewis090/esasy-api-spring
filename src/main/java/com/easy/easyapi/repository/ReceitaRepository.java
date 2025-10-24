package com.easy.easyapi.repository;

import com.easy.easyapi.model.Receita;
import com.easy.easyapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long> {

    List<Receita> findByUsuario(Usuario usuario);

    Optional<Receita> findByIdAndUsuario(Long id, Usuario usuario);
}
