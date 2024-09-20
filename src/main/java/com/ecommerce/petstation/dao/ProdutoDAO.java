package com.ecommerce.petstation.dao;

import java.sql.SQLException;
import java.util.List;
import com.ecommerce.petstation.models.Produto;

public interface ProdutoDAO extends DAO<Produto> {
    
    Produto findByIdProduto(Integer num) throws SQLException;
    // Pra mim isso nao faz sentido, voce ta puxando tds os produtos de x categoria correto?
    // Isso deveria estar na categoria e nao no produto
    List<Produto> findBySubcategoria(Integer subcategoria) throws SQLException;
    void updateEstoque(Integer novoEstoque, Integer num) throws SQLException;
    
}