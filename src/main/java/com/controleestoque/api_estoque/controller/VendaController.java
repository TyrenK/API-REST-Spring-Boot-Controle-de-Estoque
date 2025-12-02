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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final ItensVendaRepository itensVendaRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public VendaResponseDTO createVenda(@RequestBody VendaRequestDTO dto) {
        log.info("Recebendo requisição para criar venda: {}", dto);
        
        try {
            // 1) Buscar cliente
            Cliente cliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> {
                        log.error("Cliente não encontrado com ID: {}", dto.getClienteId());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Cliente não encontrado.");
                    });
            log.info("Cliente encontrado: {}", cliente.getNome());

            // Criar venda
            Venda venda = new Venda();
            venda.setCliente(cliente);
            venda.setDataVenda(LocalDateTime.now());
            venda.setItensVendas(new ArrayList<>());

            BigDecimal valorTotalVenda = BigDecimal.ZERO;
            List<ItensVenda> itensVenda = new ArrayList<>();

            // Processar cada item
            for (ItemVendaRequestDTO itemDto : dto.getItens()) {
                log.info("Processando item: produtoId={}, quantidade={}", 
                         itemDto.getProdutoId(), itemDto.getQuantidade());

                // 2) Buscar produto
                Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                        .orElseThrow(() -> {
                            log.error("Produto não encontrado com ID: {}", itemDto.getProdutoId());
                            return new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Produto com ID " + itemDto.getProdutoId() + " não encontrado.");
                        });
                log.info("Produto encontrado: {}", produto.getNome());

                // 3) Buscar estoque usando o relacionamento do produto
                // Primeira opção: através do produto.getEstoque()
                Estoque estoque = produto.getEstoque();
                
                if (estoque == null) {
                    log.error("Estoque não encontrado para produto ID: {}", produto.getId());
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Estoque não encontrado para o Produto ID " + produto.getId());
                }
                
                log.info("Estoque encontrado: quantidade={}", estoque.getQuantidade());

                int quantidadeDesejada = itemDto.getQuantidade();
                int quantidadeDisponivel = estoque.getQuantidade();

                // 4) Validar estoque
                if (quantidadeDesejada > quantidadeDisponivel) {
                    String mensagem = String.format(
                        "Estoque insuficiente para o produto: %s. Disponível: %d, Desejado: %d",
                        produto.getNome(), quantidadeDisponivel, quantidadeDesejada);
                    log.error(mensagem);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagem);
                }

                // 5) Atualizar estoque (será salvo no final da transação)
                estoque.setQuantidade(quantidadeDisponivel - quantidadeDesejada);

                // 6) Criar item de venda
                ItensVenda itemVenda = new ItensVenda();
                itemVenda.setVenda(venda);
                itemVenda.setProduto(produto);
                itemVenda.setQuantidade(quantidadeDesejada);
                itemVenda.setPrecoUnitario(itemDto.getPrecoUnitario());

                BigDecimal valorTotalItem = itemDto.getPrecoUnitario()
                        .multiply(BigDecimal.valueOf(quantidadeDesejada));
                itemVenda.setValorTotal(valorTotalItem);

                itensVenda.add(itemVenda);
                valorTotalVenda = valorTotalVenda.add(valorTotalItem);
                
                log.info("Item criado: produto={}, valorTotal={}", 
                         produto.getNome(), valorTotalItem);
            }

            // 7) Finalizar venda
            venda.setValorTotal(valorTotalVenda);
            venda.setItensVendas(itensVenda);

            // Salvar venda (cascadeará para itens se configurado corretamente)
            Venda vendaSalva = vendaRepository.save(venda);
            log.info("Venda salva com ID: {}", vendaSalva.getId());

            // Configurar a venda em cada item (bidirecional)
            for (ItensVenda item : itensVenda) {
                item.setVenda(vendaSalva);
            }
            
            // Salvar itens (se cascade não estiver funcionando)
            itensVendaRepository.saveAll(itensVenda);
            log.info("Itens da venda salvos");

            // Salvar estoques atualizados
            // Não precisa chamar save explicitamente porque estoque já é gerenciado
            // pelo EntityManager se foi carregado dentro da transação
            
            log.info("Venda criada com sucesso!");
            return convertToDTO(vendaSalva);
            
        } catch (ResponseStatusException e) {
            log.error("Erro ao criar venda: {}", e.getReason());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao criar venda", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro ao processar venda: " + e.getMessage());
        }
    }

    // Método para converter Venda para DTO
    @Transactional(readOnly = true)
    private VendaResponseDTO convertToDTO(Venda venda) {
        VendaResponseDTO dto = new VendaResponseDTO();
        dto.setId(venda.getId());
        dto.setValorTotal(venda.getValorTotal());
        dto.setDataVenda(venda.getDataVenda());

        // Dados do cliente
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setId(venda.getCliente().getId());
        clienteDTO.setNome(venda.getCliente().getNome());
        clienteDTO.setEmail(venda.getCliente().getEmail());
        dto.setCliente(clienteDTO);

        // Buscar itens explicitamente para evitar LazyInitializationException
        List<ItensVenda> itens = itensVendaRepository.findByVendaId(venda.getId());
        
        List<ItemVendaResponseDTO> itensDto = itens.stream()
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