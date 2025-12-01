package com.controleestoque.api_estoque.dto;

import java.util.List;
import lombok.Data;

@Data
public class VendaRequestDTO {

    private Long clienteId;
    private List<ItemVendaRequestDTO> itens; 
}