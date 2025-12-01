package com.controleestoque.api_estoque.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ItemVendaResponseDTO {
    
    private Long id;
    private Long produtoId;
    private String produtoNome; 
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal valorTotalItem;
}