package com.ecommerce.petstation.models;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "produto", schema = "petstation")
public class Produto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "produto_seq")
    @SequenceGenerator(name = "produto_seq", schema = "petstation", 
    sequenceName = "produto_num_seq", allocationSize = 1)
    @Column(name = "num")
    private Integer num;

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "preco", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "descricao", length = 1000)
    private String descricao;

    @Column(name = "estoque", nullable = false)
    private Integer estoque;

    @ManyToOne
    @JoinColumn(name = "id_subcategoria", nullable = false)
    private Subcategoria subcategoria;

    // Getters and Setters
    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    public Subcategoria getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(Subcategoria subcategoria) {
        this.subcategoria = subcategoria;
    }
}

/*              Script SQL
CREATE SEQUENCE petstation.produto_num_seq
	START 1 INCREMENT 1;

CREATE TABLE petstation.produto( 
    num INT DEFAULT nextval('petstation.produto_num_seq'),
    nome VARCHAR(50) NOT NULL,
    preco NUMERIC(10,2) NOT NULL,
	descricao VARCHAR(1000),
    estoque INT NOT NULL,
	id_subcategoria INT NOT NULL,
	CONSTRAINT pk_produto PRIMARY KEY(num),
	CONSTRAINT fk_subcategoria_id FOREIGN KEY(id_subcategoria)
		REFERENCES petstation.subcategoria(id_subcategoria) ON DELETE CASCADE
);
*/