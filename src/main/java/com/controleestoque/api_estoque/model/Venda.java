package com.controleestoque.api_estoque.model;

import java.util.List;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_vendas")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private BigDecimal valorTotal;
    private LocalDateTime dataVenda;
    
    // Relacionamento N:1 (Many-to-One)
    // Mapeamento: Muitas vendas têm UM cliente.
    // LAZY = Carregamento lento. Só carrega a chave estrangeira (FK).
    @ManyToOne(fetch = FetchType.LAZY) // LAZY: Carrega o cliente apenas quando for solicitada.
    @JoinColumn(name = "cliente_id", nullable = false) // Define a FK na tabela venda.
    private Cliente cliente;

    // --- Relacionamento 1:N (One-to-Many) ---
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
    private List<ItensVenda> itensVendas;

    public Venda() {}

    public Venda(Cliente cliente, List<ItensVenda> itensVendas, BigDecimal valorTotal, LocalDateTime dataVenda) {
        this.cliente = cliente;
        this.itensVendas = itensVendas;
        this.valorTotal = valorTotal;
        this.dataVenda = dataVenda;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal precoTotal) { this.valorTotal = precoTotal; }

    public LocalDateTime getDataVenda() { return dataVenda; }
    public void setDataVenda(LocalDateTime dataVenda) { this.dataVenda = dataVenda; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public List<ItensVenda> getItensVendas() { return itensVendas; }
    public void setItensVendas(List<ItensVenda> itensVendas) { this.itensVendas = itensVendas; }
}