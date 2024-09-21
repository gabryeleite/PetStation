package com.ecommerce.petstation.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PedidoProduto2DTO {

    private String nfPedido;
    private String cliente;
    private Integer quantidade;
    private BigDecimal precoTotal;
    private LocalDate data;

    public String getNfPedido() {
        return nfPedido;
    }

    public void setNfPedido(String nfPedido) {
        this.nfPedido = nfPedido;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public BigDecimal getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(BigDecimal precoTotal) {
        this.precoTotal = precoTotal;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
