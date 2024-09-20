package com.ecommerce.petstation.daos;

import java.sql.SQLException;
import java.util.List;
import com.ecommerce.petstation.models.Produto;

public interface ProdutoDAO extends DAO<Produto> {
    
    void updateEstoque(Integer novoEstoque, Integer num) throws SQLException;
    List<Produto> findMaisVendidos() throws SQLException;

}