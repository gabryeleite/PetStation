package com.ecommerce.petstation.dtos;

import java.math.BigDecimal;

public class ClienteTicketDTO {
    
    private String nome;
    private BigDecimal ticketMedio;

    public ClienteTicketDTO(String nome, BigDecimal ticketMedio) {
        this.nome = nome;
        this.ticketMedio = ticketMedio;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getTicketMedio() {
        return ticketMedio;
    }

    public void setTicketMedio(BigDecimal ticketMedio) {
        this.ticketMedio = ticketMedio;
    }

}

