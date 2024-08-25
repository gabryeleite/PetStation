package com.ecommerce.petstation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.petstation.models.Carrinho;

import jakarta.transaction.Transactional;

@Repository
public interface CarrinhoRepository extends JpaRepository<Carrinho, Integer> {
    
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO compra_item (num_pedido, num_produto, qnt_produto)" +
                   " VALUES (:num_pedido, :num_produto, :qnt_produto)", nativeQuery = true)
    void createCarrinho(
        @Param("num_pedido") Integer numPedido,
        @Param("num_produto") Integer numProduto,
        @Param("qnt_produto") Integer qntProduto
    );

}
