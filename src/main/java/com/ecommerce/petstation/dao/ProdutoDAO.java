package com.ecommerce.petstation.dao;

import java.sql.SQLException;
import java.util.List;
import com.ecommerce.petstation.models.Produto;

public interface ProdutoDAO extends DAO<Produto> {
    
    Produto findByIdProduto(Integer num) throws SQLException;
    void updateEstoque(Integer novoEstoque, Integer num) throws SQLException;
    List<Produto> findMaisVendidos() throws SQLException;

}