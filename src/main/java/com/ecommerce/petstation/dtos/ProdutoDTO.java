package com.ecommerce.petstation.dtos;

import java.time.LocalDate;

public class ProdutoDTO {

    private String nomeProduto;
    private Integer quantidade;
    private String nfPedido;
    private LocalDate dataPedido;

    // Getters e Setters
    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getNfPedido() {
        return nfPedido;
    }

    public void setNfPedido(String nfPedido) {
        this.nfPedido = nfPedido;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }
}