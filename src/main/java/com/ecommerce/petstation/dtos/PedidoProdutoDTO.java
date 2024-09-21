package com.ecommerce.petstation.dtos;

import java.math.BigDecimal;

public class PedidoProdutoDTO {

    private String nomeProduto;
    private String nfPedido;
    private Integer numProduto;
    private Integer quantidade;
    private BigDecimal preco;
    private BigDecimal total;

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

    public Integer getNumProduto() {
        return numProduto;
    }

    public void setNumProduto(Integer numProduto) {
        this.numProduto = numProduto;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
