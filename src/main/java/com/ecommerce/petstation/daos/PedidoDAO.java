package com.ecommerce.petstation.daos;

import java.sql.SQLException;
import java.util.List;

import com.ecommerce.petstation.models.Pedido;

public interface PedidoDAO extends DAO<Pedido>{
    List<Pedido> findByCliente(String cpfCliente) throws SQLException;
    Pedido findByNF(String nf) throws SQLException;
}
