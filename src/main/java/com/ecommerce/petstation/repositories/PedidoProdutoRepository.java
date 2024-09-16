package com.ecommerce.petstation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.petstation.models.PedidoProduto;

@Repository
public interface PedidoProdutoRepository extends JpaRepository<PedidoProduto, Integer> {
    
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO compra_item (num_pedido, num_produto, qnt_produto, preco_produto)" +
                   " VALUES (:num_pedido, :num_produto, :qnt_produto, :preco_produto)", nativeQuery = true)
    void createPedidoProduto(
        @Param("num_pedido") Integer numPedido,
        @Param("num_produto") Integer numProduto,
        @Param("qnt_produto") Integer qntProduto,
        @Param("preco_produto") Integer precoProduto
    );

}
