package com.controleestoque.api_estoque.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ItemVendaRequestDTO {
    
    private Long produtoId;
    private Integer quantidade; 
    private BigDecimal precoUnitario; 
}