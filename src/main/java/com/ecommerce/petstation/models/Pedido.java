package com.ecommerce.petstation.models;

import java.time.LocalDate;
import java.time.LocalTime;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Pedido {
    
    private String notaFiscal;

    private String cpfCliente;

    private LocalDate dataPedido;

    private LocalTime horaPedido;

    @JsonIgnore
    private List<PedidoProduto> itensPedido;

    // Getters and Setters

    public String getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(String notaFiscal) {
        this.notaFiscal = notaFiscal;
    }

    public String getCpfCliente() {
        return cpfCliente;
    }

    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
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

    public List<PedidoProduto> getitensPedido() {
        return itensPedido;
    }

    public void setitensPedido(List<PedidoProduto> itensPedido) {
        this.itensPedido = itensPedido;
    }
}

/*             Script SQL
CREATE SEQUENCE petstation.pedido_num_seq
	START 1 INCREMENT 1;

CREATE TABLE petstation.pedido(
    num INT DEFAULT nextval('petstation.pedido_num_seq'),
    id_cliente INT NOT NULL,
    data_pedido DATE NOT NULL,
    hora_pedido TIME NOT NULL,
    CONSTRAINT pk_pedido PRIMARY KEY(num),
    CONSTRAINT fk_cliente_id FOREIGN KEY(id_cliente)
        REFERENCES petstation.cliente(id_cliente) ON DELETE CASCADE,
);
*/