package com.ecommerce.petstation.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class PedidoRequestDTO {


    @JsonProperty
    private String cpfCliente;

    @JsonProperty
    private Set<ProdutoRequestDTO> produtos;

    public String getCpfCliente() {
        return cpfCliente;
    }

    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
    }

    public Set<ProdutoRequestDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(Set<ProdutoRequestDTO> produtos) {
        this.produtos = produtos;
    }
}