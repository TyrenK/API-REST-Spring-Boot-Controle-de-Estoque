package com.controleestoque.api_estoque.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.controleestoque.api_estoque.model.ItensVenda;

@Repository
public interface ItensVendaRepository extends JpaRepository<ItensVenda, Long> {
    List<ItensVenda> findByVendaId(Long vendaId);
}