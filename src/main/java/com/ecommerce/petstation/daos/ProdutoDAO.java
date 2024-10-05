package com.ecommerce.petstation.daos;

import java.sql.SQLException;
import java.util.List;

import com.ecommerce.petstation.dtos.ProdutoNFDTO;
import com.ecommerce.petstation.dtos.ProdutoDTO;
import com.ecommerce.petstation.dtos.ProdutoVendidoDTO;
import com.ecommerce.petstation.models.Produto;

public interface ProdutoDAO extends DAO<Produto> {
    
    List<Produto> findByCategoria(Integer idCategoria) throws SQLException;
    List<ProdutoDTO> findByCliente(String cpfCliente) throws SQLException;
    void updateEstoque(Integer novoEstoque, Integer num) throws SQLException;
    List<ProdutoVendidoDTO> findMaisVendidos() throws SQLException;
    List<ProdutoVendidoDTO> findMaisVendidosPorCategoria() throws SQLException;
    List<Produto> findByTermo(String termo) throws SQLException;
    List<ProdutoNFDTO> findByNotaFiscal(String notaFiscal) throws SQLException;

}