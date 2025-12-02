package com.controleestoque.api_estoque.repository;

import com.controleestoque.api_estoque.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    
    Optional<Estoque> findByProduto_Id(Long produtoId);
    

    @Query("SELECT e FROM Estoque e WHERE e.produto.id = :produtoId")
    Optional<Estoque> buscarPorProdutoId(@Param("produtoId") Long produtoId);
}