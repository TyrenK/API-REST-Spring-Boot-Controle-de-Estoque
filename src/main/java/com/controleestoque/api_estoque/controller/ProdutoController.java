package com.controleestoque.api_estoque.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.controleestoque.api_estoque.dto.ProdutoResponseDTO;
import com.controleestoque.api_estoque.model.Produto;
import com.controleestoque.api_estoque.repository.ProdutoRepository;

import lombok.RequiredArgsConstructor;

import com.controleestoque.api_estoque.repository.CategoriaRepository;
import com.controleestoque.api_estoque.repository.FornecedorRepository;


@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final FornecedorRepository fornecedorRepository;

    @GetMapping
    public List<ProdutoResponseDTO> getAllProdutos() {
        return produtoRepository.findAll()
                .stream()
                .map(ProdutoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> getCategoriaById(@PathVariable Long id) {
        return produtoRepository.findById(id)
                .map(produto -> ResponseEntity.ok(ProdutoResponseDTO.fromEntity(produto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Produto> createProduto(@RequestBody Produto produto) {
        if (produto.getCategoria() == null || produto.getCategoria().getId() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        categoriaRepository.findById(produto.getCategoria().getId())
                .ifPresentOrElse(produto::setCategoria, () -> {
                    throw new IllegalArgumentException("Categoria não encontrada");
                });

        if (produto.getFornecedores() != null && !produto.getFornecedores().isEmpty()) {
            produto.setFornecedores(produto.getFornecedores().stream()
                    .map(fornecedor -> fornecedorRepository.findById(fornecedor.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Fornecedor não encontrado")))
                    .collect(Collectors.toSet()));
        }

        if (produto.getEstoque() != null) {
            produto.getEstoque().setProduto(produto);
        }

        Produto savedProduto = produtoRepository.save(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Produto> updateProduto(@PathVariable Long id, @RequestBody Produto produtoDetails) {

        return produtoRepository.findById(id)
                .map(produto -> {

                    produto.setNome(produtoDetails.getNome());
                    Produto updatedProduto = produtoRepository.save(produto);

                    return ResponseEntity.ok(updatedProduto);
                })

                .orElse(ResponseEntity.notFound().build());
    }

    


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {

        if (!produtoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        produtoRepository.deleteById(id);
        return ResponseEntity.noContent().build(); 
    }
}
