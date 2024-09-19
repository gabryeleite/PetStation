package com.ecommerce.petstation.dao;

import java.sql.SQLException;
import java.util.List;
import com.ecommerce.petstation.models.Produto;

public interface ProdutoDAO extends DAO<Produto> {
    
    Produto findByIdProduto(Integer num) throws SQLException;
    List<Produto> findBySubcategoria(Integer subcategoria) throws SQLException;
    //void createProduto(String nome, BigDecimal preco, String descricao, Integer estoque, Integer idSubcategoria) throws SQLException;
    void updateEstoque(Integer novoEstoque, Integer num) throws SQLException;
    
}