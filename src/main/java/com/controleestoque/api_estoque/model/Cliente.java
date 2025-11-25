package com.controleestoque.api_estoque.model;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String email;

    // --- Relacionamento 1:N (One-to-Many) ---
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Venda> vendas;

    // Construtores, Getters e Setters...
    public Cliente() {}

    public Cliente(String nome, String email, List<Venda> vendas) {
        this.nome = nome;
        this.email = email;
        this.vendas = vendas;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public List<Venda> getVenda() { return vendas; }

    public void setVenda(List<Venda> vendas) { this.vendas = vendas; }
}
