package com.ecommerce.petstation.repositories;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.petstation.models.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    
    @Query(value = "SELECT * FROM cliente ORDER BY nome ASC", nativeQuery = true)
    List<Cliente> findAll();

    @Query(value = "SELECT * FROM cliente WHERE id_cliente = (:id_cliente)", nativeQuery = true)
    Optional<Cliente> findById(@Param("id_cliente") Integer id_cliente);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO cliente (nome, sobrenome, sexo, data_nascimento, cpf, email)" + 
                   " VALUES (:nome, :sobrenome, :sexo, :data_nascimento, :cpf, :email)", nativeQuery = true)
    void createCliente(
            @Param("nome") String nome,
            @Param("sobrenome") String sobrenome,
            @Param("sexo") String sexo,
            @Param("data_nascimento") LocalDate dataNascimento,
            @Param("cpf") String cpf,
            @Param("email") String email
    );
    
}
