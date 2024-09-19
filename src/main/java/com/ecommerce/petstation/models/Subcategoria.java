package com.ecommerce.petstation.models;

public class Subcategoria {

    private Integer idSubcategoria;

    private String nome;

    private Integer idCategoria;

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

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
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