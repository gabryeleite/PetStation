package com.ecommerce.petstation.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "categoria", schema = "petstation", 
    uniqueConstraints = {@UniqueConstraint(columnNames = "nome")})
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categoria_seq")
    @SequenceGenerator(name = "categoria_seq", schema = "petstation", 
    sequenceName = "categoria_id_seq", allocationSize = 1)
    @Column(name = "id_categoria")
    private Integer idCategoria; 

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    // Getters and Setters
    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

/*              Scprit SQL
CREATE SEQUENCE petstation.categoria_id_seq
	START 1 INCREMENT 1;

CREATE TABLE petstation.categoria(
	id_categoria INT DEFAULT nextval('petstation.categoria_id_seq'),
	nome VARCHAR(50) NOT NULL,
	CONSTRAINT pk_categoria PRIMARY KEY(id_categoria),
	CONSTRAINT uk_categoria_nome UNIQUE(nome)
); 
*/