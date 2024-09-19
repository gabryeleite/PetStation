package com.ecommerce.petstation.models;

import java.math.BigDecimal;

public class PedidoProduto {

    private Integer id;

    private Integer numPedido;

    private Integer numProduto;

    private Integer qntProduto;

    private BigDecimal precoProduto;

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(Integer numPedido) {
        this.numPedido = numPedido;
    }

    public Integer getQntProduto() {
        return qntProduto;
    }

    public void setQntProduto(Integer qntProduto) {
        this.qntProduto = qntProduto;
    }

    public BigDecimal getPrecoProduto() {
        return precoProduto;
    }

    public void setPrecoProduto(BigDecimal precoProduto) {
        this.precoProduto = precoProduto;
    }

    public Integer getNumProduto() {
        return numProduto;
    }

    public void setNumProduto(Integer numProduto) {
        this.numProduto = numProduto;
    }

}

/*             Script SQL
CREATE TABLE petstation.pedido_produto(
    qnt_produto INT NOT NULL,
    num_pedido INT NOT NULL,
    num_produto INT NOT NULL,
    preco_produto NUMERIC(10,2) NOT NULL,
    CONSTRAINT pk_pedido_produto PRIMARY KEY(num_pedido, num_produto),
    CONSTRAINT fk_pedido_num FOREIGN KEY(num_pedido)
        REFERENCES petstation.pedido(num) ON DELETE CASCADE,
    CONSTRAINT fk_produto_num FOREIGN KEY(num_produto)
        REFERENCES petstation.produto(num) ON DELETE CASCADE,
    CONSTRAINT ck_qnt_produto CHECK (qnt_produto > 0)   
);
*/