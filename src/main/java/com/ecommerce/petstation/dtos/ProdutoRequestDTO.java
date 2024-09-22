package com.ecommerce.petstation.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ProdutoRequestDTO {

    @JsonProperty
    private Integer numProduto;

    @JsonProperty
    private Integer qntComprada;

    private BigDecimal precoTotal;

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

    public BigDecimal getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(BigDecimal precoTotal) {
        this.precoTotal = precoTotal;
    }


}