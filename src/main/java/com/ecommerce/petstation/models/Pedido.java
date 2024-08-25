package com.ecommerce.petstation.models;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
@Table(name = "pedido", schema = "petstation")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pedido_seq")
    @SequenceGenerator(name = "pedido_seq", schema = "petstation", 
    sequenceName = "pedido_num_seq", allocationSize = 1)
    @Column(name = "num")
    private Integer num;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @Column(name = "data_pedido", nullable = false)
    private LocalDate dataPedido;

    @Column(name = "hora_pedido", nullable = false)
    private LocalTime horaPedido;

    // Getters and Setters
    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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