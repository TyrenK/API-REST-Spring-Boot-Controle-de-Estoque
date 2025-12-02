package com.controleestoque.api_estoque.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProdutoRequestDTO{
    private String nome;
    private BigDecimal preco;
    private Long categoriaId;
    private Integer quantidadeEstoque;
} 
