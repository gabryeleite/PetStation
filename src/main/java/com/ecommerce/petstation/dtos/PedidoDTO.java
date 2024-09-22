package com.ecommerce.petstation.dtos;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import com.ecommerce.petstation.models.PedidoProduto;

import java.time.LocalDate;

public class PedidoDTO {
    
    private String cpfCliente;
    private String notaFiscal;
    private LocalDate dataPedido;
    private LocalTime horaPedido;
    private BigDecimal precoTotal;

    private Set<PedidoProduto> itensPedido = new HashSet<>();

    // Getters and Setters

    public String getCpfCliente() {
        return cpfCliente;
    }
    
    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
    }

    public String getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(String notaFiscal) {
        this.notaFiscal = notaFiscal;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public LocalTime getHoraPedido() {
        return horaPedido;
    }

    public void setHoraPedido(LocalTime horaPedido) {
        this.horaPedido = horaPedido;
    }

    public BigDecimal getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(BigDecimal precoTotal) {
        this.precoTotal = precoTotal;
    }

    public Set<PedidoProduto> getItensPedido() {
        return itensPedido;
    }

    public void setItensPedido(Set<PedidoProduto> itensPedido) {
        this.itensPedido = itensPedido;
    }

}
