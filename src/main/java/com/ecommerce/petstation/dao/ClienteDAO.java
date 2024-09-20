package com.ecommerce.petstation.dao;

import java.sql.SQLException;
import java.util.List;

import com.ecommerce.petstation.models.Cliente;

public interface ClienteDAO extends DAO<Cliente> {
    Cliente findByIdCliente(Integer idCliente) throws SQLException;
    // Cliente findByCpfCliente(Integer idCliente) throws SQLException;
    List<Cliente> findClientesComMaisPedidos() throws SQLException;

}

