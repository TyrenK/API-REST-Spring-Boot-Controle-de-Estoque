package com.controleestoque.api_estoque.model;

import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_itensVenda")
public class ItensVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private BigDecimal quantidadeV;
    private BigDecimal precoUni;

    // Relacionamento 1:1 (One-to-One)
    // Mapeamento: Um item vendido têm UMA venda.
    // LAZY = Carregamento lento. Só carrega a chave estrangeira (FK).
    @OneToOne(fetch = FetchType.LAZY) // LAZY: Carrega a venda apenas quando for solicitada.
    @JoinColumn(name = "venda_id", nullable = false) // Define a FK na tabela itensvenda.
    private Venda venda;


    // Relacionamento N:1 (Many-to-One)
    // Mapeamento: Um item vendido têm UM produto.
    // LAZY = Carregamento lento. Só carrega a chave estrangeira (FK).
    @OneToOne(fetch = FetchType.LAZY) // LAZY: Carrega o produto apenas quando for solicitada.
    @JoinColumn(name = "produto_id", nullable = false) // Define a FK na tabela itensvenda.
    private Produto produto;

    // Construtores, Getters e Setters...
    public ItensVenda() {}

    public ItensVenda(BigDecimal quantidadeV, BigDecimal precoUni, Venda venda, Produto produto) {
        this.quantidadeV = quantidadeV;
        this.precoUni = precoUni;
        this.venda = venda;
        this.produto = produto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getQuantidadeV() { return quantidadeV; }
    public void setQuantidadeV(BigDecimal quantidadeV) { this.quantidadeV = quantidadeV; }

    public BigDecimal getPrecoUni() { return precoUni; }
    public void setPrecoUni(BigDecimal precoUni) { this.precoUni = precoUni; }

    public Venda venda() { return venda; }
    public void setVenda(Venda venda) { this.venda = venda; }

    public Produto produto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

}