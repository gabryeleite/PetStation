package com.ecommerce.petstation.models;

import java.math.BigDecimal;

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
@Table(name = "pedido_produto", schema = "petstation")
public class PedidoProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pedido_produto_seq")
    @SequenceGenerator(name = "pedido_produto_seq", schema = "petstation", 
    sequenceName = "pedido_produto_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @Column(name = "num_pedido")
    private Integer numPedido;

    @Column(name = "num_produto")
    private Integer numProduto;

    @Column(name = "qnt_produto", nullable = false)
    private Integer qntProduto;

    @Column(name = "preco", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoProduto;

    @ManyToOne
    @JoinColumn(name = "num_pedido", insertable = false, updatable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "num_produto", insertable = false, updatable = false)
    private Produto produto;

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

    public BigDecimal getPrecoProduto() {
        return precoProduto;
    }

    public void setPrecoProduto(BigDecimal preco) {
        this.precoProduto = preco;
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