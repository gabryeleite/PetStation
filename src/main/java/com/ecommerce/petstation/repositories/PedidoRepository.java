package com.ecommerce.petstation.repositories;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.petstation.models.Pedido;
import com.ecommerce.petstation.models.Produto;

import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    
    @Query(value = "SELECT * FROM produto WHERE id_cliente = (:id_cliente)", nativeQuery = true)
    Produto findByCLiente(@Param("id_cliente") Integer id_cliente);

    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Integer findLastInsertId();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO compra (id_cliente, data_pedido, horario_pedido)" +
                   " VALUES (:id_cliente, :data_pedido, :horario_pedido)", nativeQuery = true)
    void createCompra(
            @Param("id_cliente") Integer cliente,
            @Param("data_pedido") LocalDate dataPedido,
            @Param("horario_pedido") LocalTime horarioPedido
    );

    // precoTotal

}
