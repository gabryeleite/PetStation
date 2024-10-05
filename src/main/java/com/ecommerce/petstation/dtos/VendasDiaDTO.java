package com.ecommerce.petstation.dtos;

import java.time.LocalDate;

public class VendasDiaDTO {
    private LocalDate data;
    private Integer quantidadeTotal;

    // Getters e Setters
    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Integer getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public void setQuantidadeTotal(Integer quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
    }
}
