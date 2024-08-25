package com.ecommerce.petstation.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "carrinho", schema = "petstation")
public class Carrinho {
    @Id
    @Column(name = "num_pedido")
    private Integer numPedido;

    @Id
    @Column(name = "num_produto")
    private Integer numProduto;

    @Column(name = "qnt_produto", nullable = false)
    private Integer qntProduto;

    @ManyToOne
    @JoinColumn(name = "num_pedido", insertable = false, updatable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "num_produto", insertable = false, updatable = false)
    private Produto produto;

    // Getters and Setters
    public Integer getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(Integer numPedido) {
        this.numPedido = numPedido;
    }

    public Integer getNumProduto() {
        return numProduto;
    }

    public void setNumProduto(Integer numProduto) {
        this.numProduto = numProduto;
    }

    public Integer getQntProduto() {
        return qntProduto;
    }

    public void setQntProduto(Integer qntProduto) {
        this.qntProduto = qntProduto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}

/*             Script SQL
CREATE TABLE petstation.carrinho(
    qnt_produto INT NOT NULL,
    num_pedido INT NOT NULL,
    num_produto INT NOT NULL,
    CONSTRAINT pk_carrinho PRIMARY KEY(num_pedido, num_produto),
    CONSTRAINT fk_pedido_num FOREIGN KEY(num_pedido)
        REFERENCES petstation.pedido(num) ON DELETE CASCADE,
    CONSTRAINT fk_produto_num FOREIGN KEY(num_produto)
        REFERENCES petstation.produto(num) ON DELETE CASCADE,
    CONSTRAINT ck_qnt_produto CHECK (qnt_produto > 0)   
);
*/