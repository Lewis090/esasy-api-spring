package com.easy.easyapi.repository;

import com.easy.easyapi.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DespesaRepository extends JpaRepository<Despesa, Long> { }
