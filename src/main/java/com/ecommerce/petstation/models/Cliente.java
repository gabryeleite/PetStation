package com.ecommerce.petstation.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.SequenceGenerator;

@Entity
@Table(name = "cliente", schema = "petstation",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "telefone"),
        @UniqueConstraint(columnNames = "email")
    })
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cliente_seq")
    @SequenceGenerator(name = "cliente_seq", schema = "petstation", 
    sequenceName = "cliente_id_seq", allocationSize = 1)
    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "sobrenome", nullable = false, length = 100)
    private String sobrenome;

    @Column(name = "sexo", length = 1)
    private String sexo;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "telefone", nullable = false, length = 14)
    private String telefone;

    @Column(name = "email", nullable = false, length = 80)
    private String email;

    // Getters and Setters
    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

/*              Script SQL
CREATE SEQUENCE petstation.cliente_id_seq
	START 1 INCREMENT 1;

CREATE TABLE petstation.cliente(
    id_cliente INT DEFAULT nextval('petstation.cliente_id_seq'),
    nome VARCHAR(50) NOT NULL,
    sobrenome VARCHAR(100) NOT NULL,
    sexo CHAR(1),
    data_nascimento DATE NOT NULL,
    telefone CHAR(14) NOT NULL,
    email VARCHAR(80) NOT NULL,
    CONSTRAINT pk_cliente  PRIMARY KEY(id_cliente),
    CONSTRAINT uk_cliente_telefone UNIQUE(telefone),
    CONSTRAINT uk_cliente_email UNIQUE(email),
    CONSTRAINT ck_cliente_sexo CHECK(sexo in ('M','F')), 
    CONSTRAINT ck_cliente_telefone 
		CHECK (telefone ~ '^\([0-9]{2}\)9[0-9]{4}-[0-9]{4}$') 
);
*/