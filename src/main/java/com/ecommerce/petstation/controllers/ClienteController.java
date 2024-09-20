package com.ecommerce.petstation.controllers;

import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.petstation.dao.PgClienteDAO;
import com.ecommerce.petstation.models.Cliente;

@RestController
@RequestMapping(value="/clientectr")
public class ClienteController {

    @Autowired
    private PgClienteDAO pgClienteDAO;

    @GetMapping(value = "/clientes")
    public List<Cliente> clientes() {
        try {
            return pgClienteDAO.all();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/mais-pedidos")
    public List<Cliente> getClientesComMaisPedidos() throws SQLException {
        try {
            return pgClienteDAO.findClientesComMaisPedidos();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
}

/*
    @GetMapping(value="/produto/{id}")
    public Cliente produtoById(@PathVariable(value="id") Integer id) {
        try {
            return pgClienteDAO.findByIdCliente(id);
        } catch (SQLException e) {
            // Gerenciar exceções adequadamente
            e.printStackTrace();
            return null;
        }
    }
}

*/
