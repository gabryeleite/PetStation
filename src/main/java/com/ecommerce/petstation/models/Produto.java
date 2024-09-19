package com.ecommerce.petstation.models;

import java.math.BigDecimal;

public class Produto {

    private Integer num;

    private String nome;

    private BigDecimal preco;

    private String descricao;

    private Integer estoque;

    private Integer idSubcategoria;

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

    public Integer getIdSubcategoria() {
        return idSubcategoria;
    }

    public void setIdSubcategoria(Integer idSubcategoria) {
        this.idSubcategoria = idSubcategoria;
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
    CONSTRAINT uk_produto UNIQUE(nome);
	CONSTRAINT pk_produto PRIMARY KEY(num),
	CONSTRAINT fk_subcategoria_id FOREIGN KEY(id_subcategoria)
		REFERENCES petstation.subcategoria(id_subcategoria) ON DELETE CASCADE
);
*/