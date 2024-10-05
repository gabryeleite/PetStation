package com.ecommerce.petstation.dtos;

import java.math.BigDecimal;

public class ProdutoTicketDTO {

    private String nomeProduto;
    private BigDecimal ticketMedio;

    public ProdutoTicketDTO(String nomeProduto, BigDecimal ticketMedio) {
        this.nomeProduto = nomeProduto;
        this.ticketMedio = ticketMedio;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public BigDecimal getTicketMedio() {
        return ticketMedio;
    }

    public void setTicketMedio(BigDecimal ticketMedio) {
        this.ticketMedio = ticketMedio;
    }
}
    
