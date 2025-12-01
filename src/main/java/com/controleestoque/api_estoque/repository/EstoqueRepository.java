package com.controleestoque.api_estoque.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.controleestoque.api_estoque.model.Estoque;
import com.controleestoque.api_estoque.model.Produto;
import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long>{
    Optional<Estoque> findByProduto(Produto produto);
}
