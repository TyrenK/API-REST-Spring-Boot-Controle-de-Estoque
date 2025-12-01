package com.controleestoque.api_estoque.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException; 

import com.controleestoque.api_estoque.dto.*; 
import com.controleestoque.api_estoque.model.*;
import com.controleestoque.api_estoque.repository.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final EstoqueRepository estoqueRepository; 
    private final ItensVendaRepository itensVendaRepository; 

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public VendaResponseDTO createVenda(@RequestBody VendaRequestDTO dto) {

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));

        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setDataVenda(LocalDateTime.now());
        BigDecimal valorTotalVenda = BigDecimal.ZERO;

        List<ItensVenda> itensVenda = new ArrayList<>();

        for (ItemVendaRequestDTO itemDto : dto.getItens()) {

            Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Produto com ID " + itemDto.getProdutoId() + " não encontrado."));

            Estoque estoque = estoqueRepository.findByProduto(produto) 
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Estoque não encontrado para o Produto ID " + produto.getId()));

            int quantidadeDesejada = itemDto.getQuantidade();
            int quantidadeDisponivel = estoque.getQuantidade();

            if (quantidadeDesejada > quantidadeDisponivel) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Estoque insuficiente para o produto: " + produto.getNome() + ". Disponível: " + quantidadeDisponivel + ", Desejado: " + quantidadeDesejada);
            }


            ItensVenda itemVenda = new ItensVenda();
            itemVenda.setVenda(venda); 
            itemVenda.setProduto(produto);
            itemVenda.setQuantidade(quantidadeDesejada);
            itemVenda.setPrecoUnitario(itemDto.getPrecoUnitario());
            
            BigDecimal valorTotalItem = itemDto.getPrecoUnitario().multiply(new BigDecimal(quantidadeDesejada));
            itemVenda.setValorTotal(valorTotalItem); 
            
            itensVenda.add(itemVenda);
            valorTotalVenda = valorTotalVenda.add(valorTotalItem);

            estoque.setQuantidade(quantidadeDisponivel - quantidadeDesejada);
            estoqueRepository.save(estoque); 
        }

        venda.setValorTotal(valorTotalVenda);
        venda.setItensVendas(itensVenda); 
        Venda vendaSalva = vendaRepository.save(venda);

        itensVendaRepository.saveAll(itensVenda); 

        return convertToDTO(vendaSalva);
    }
    
    
    private VendaResponseDTO convertToDTO(Venda venda) {
        VendaResponseDTO dto = new VendaResponseDTO();
        dto.setId(venda.getId());
        dto.setClienteId(venda.getCliente().getId());
        dto.setValorTotal(venda.getValorTotal());
        dto.setDataVenda(venda.getDataVenda());
        
        List<ItemVendaResponseDTO> itensDto = venda.getItensVendas().stream()
            .map(item -> {
                ItemVendaResponseDTO itemDto = new ItemVendaResponseDTO();
                itemDto.setId(item.getId());
                itemDto.setProdutoId(item.getProduto().getId());
                itemDto.setProdutoNome(item.getProduto().getNome());
                itemDto.setQuantidade(item.getQuantidade());
                itemDto.setPrecoUnitario(item.getPrecoUnitario());
                itemDto.setValorTotalItem(item.getValorTotal());
                return itemDto;
            })
            .collect(Collectors.toList());
            
        dto.setItens(itensDto);
        return dto;
    }
}