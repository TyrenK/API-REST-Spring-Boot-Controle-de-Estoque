package com.controleestoque.api_estoque.model;


import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_itensVenda")
public class ItensVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal valorTotal;


    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "venda_id", nullable = false) 
    private Venda venda;


    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "produto_id", nullable = false) 
    private Produto produto;

    // Construtores, Getters e Setters...
    public ItensVenda() {}

    public ItensVenda(Integer quantidade, BigDecimal precoUnitario, Venda venda, Produto produto, BigDecimal valorTotal) {
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.venda = venda;
        this.produto = produto;
        this.valorTotal = valorTotal;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal precoTotal) { this.valorTotal = precoTotal; }

    public Venda getVenda() { return venda; }
    public void setVenda(Venda venda) { this.venda = venda; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

}