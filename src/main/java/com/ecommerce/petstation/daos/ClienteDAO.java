package com.ecommerce.petstation.daos;

import java.sql.SQLException;
import java.util.List;

import com.ecommerce.petstation.models.Cliente;

public interface ClienteDAO extends DAO<Cliente> {

    void delete(String cpf) throws SQLException;
    Cliente findByCpfCliente(String cpf) throws SQLException;
    List<Cliente> findClientesComMaisPedidos() throws SQLException;
}

