package com.controleestoque.api_estoque.dto;

import java.math.BigDecimal;

import com.controleestoque.api_estoque.model.Produto;

import lombok.Data;

@Data
public class ProdutoResponseDTO {
    private Long id;
    private String nome;
    private BigDecimal preco;
    private Long categoriaId;
    private String categoriaNome;
    private Integer quantidadeEstoque;

    public static ProdutoResponseDTO fromEntity(Produto produto) {
        ProdutoResponseDTO dto = new ProdutoResponseDTO();

        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setPreco(produto.getPreco());

        if (produto.getCategoria() != null) {
            dto.setCategoriaId(produto.getCategoria().getId());
            dto.setCategoriaNome(produto.getCategoria().getNome());
        }

        if (produto.getEstoque() != null) {
            dto.setQuantidadeEstoque(produto.getEstoque().getQuantidade());
        }

        return dto;
    }
}
