package com.ecommerce.petstation.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProdutoRequestDTO {

    @JsonProperty
    private Integer numProduto;

    @JsonProperty
    private Integer qntComprada;

    // Getters and Setters

    public Integer getNumProduto() {
        return numProduto;
    }

    public void setNumProduto(Integer numProduto) {
        this.numProduto = numProduto;
    }

    public Integer getQntComprada() {
        return qntComprada;
    }

    public void setQntComprada(Integer qntComprada) {
        this.qntComprada = qntComprada;
    }

}