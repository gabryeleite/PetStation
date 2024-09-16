package com.ecommerce.petstation.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
@Table(name = "subcategoria", schema = "petstation")
public class Subcategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subcategoria_seq")
    @SequenceGenerator(name = "subcategoria_seq", schema = "petstation", 
    sequenceName = "subcategoria_id_seq", allocationSize = 1)
    @Column(name = "id_subcategoria")
    private Integer idSubcategoria;

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    // Getters and Setters
    
    public Integer getIdSubcategoria() {
        return idSubcategoria;
    }

    public void setIdSubcategoria(Integer idSubcategoria) {
        this.idSubcategoria = idSubcategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}

/*              Script SQL
CREATE SEQUENCE petstation.subcategoria_id_seq
	START 1 INCREMENT 1;

CREATE TABLE petstation.subcategoria(
	id_subcategoria INT DEFAULT nextval('petstation.subcategoria_id_seq'),
	nome VARCHAR(50) NOT NULL,
	id_categoria,
	CONSTRAINT pk_categoria PRIMARY KEY(id_subcategoria),
	CONSTRAINT fk_categoria_id FOREIGN KEY(id_categoria)
		REFERENCES petstation.categoria(id_categoria) ON DELETE CASCADE
);
*/