package com.controleestoque.api_estoque.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class VendaResponseDTO {

    private Long id;
    private ClienteDTO cliente; // incluir apenas dados do cliente, sem vendas
    private LocalDateTime dataVenda; 
    private BigDecimal valorTotal;
    private List<ItemVendaResponseDTO> itens;
}
