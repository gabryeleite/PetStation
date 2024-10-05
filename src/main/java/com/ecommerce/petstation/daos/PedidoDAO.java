package com.ecommerce.petstation.daos;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.ecommerce.petstation.dtos.VendasDiaDTO;
import com.ecommerce.petstation.models.Pedido;

public interface PedidoDAO extends DAO<Pedido>{
    List<Pedido> findByCliente(String cpfCliente) throws SQLException;
    Pedido findByNF(String nf) throws SQLException;
    List<VendasDiaDTO> findByVendasPorDia(LocalDate inicio, LocalDate fim) throws SQLException;
}
