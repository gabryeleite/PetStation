package com.ecommerce.petstation.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.petstation.models.Produto;

import jakarta.transaction.Transactional;

@Repository 
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    @Query(value = "SELECT * FROM produto ORDER BY num DESC", nativeQuery = true)
    List<Produto> findAll();

    @Query(value = "SELECT * FROM produto WHERE num = (:num)", nativeQuery = true)
    Produto findByIdProduto(@Param("num") Integer num);
    
    @Query(value = "SELECT * FROM produto WHERE id_subcategoria = (:id_subcategoria)", nativeQuery = true)
    List<Produto> findBySubcategoria(@Param("id_subcategoria") Integer subcategoria);

    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Integer findLastInsertId();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO produto (nome, preco, descricao, estoque, id_subcategoria)" +
                   " VALUES (:nome, :preco, :descricao, :estoque, :id_subcategoria)", nativeQuery = true)
    void createProduto(
        @Param("nome") String nome,
        @Param("preco") BigDecimal preco,
        @Param("descricao") String descricao,
        @Param("estoque") Integer estoque,
        @Param("id_subcategoria") Integer subcategoria
    );

    @Modifying
    @Transactional
    @Query(value = "UPDATE produto SET estoque = (:novo_estoque) WHERE num = (:num)", nativeQuery = true)
    void updateEstoque(@Param("novo_estoque") Integer novoEstoque, @Param("num") Integer num);

}
