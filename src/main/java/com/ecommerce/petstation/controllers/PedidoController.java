package com.ecommerce.petstation.controllers;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.petstation.daos.daosPg.PgPedidoDAO;
import com.ecommerce.petstation.models.Pedido;

@RestController
@RequestMapping(value="/api")
public class PedidoController {
    
    @Autowired
    private PgPedidoDAO pgPedidoDAO;

    @GetMapping(value = "/pedido/{id}")
    public Pedido readPedido(@PathVariable(value="id") Integer id) {
        try {
            return pgPedidoDAO.read(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping(value = "/pedidos")
    public List<Pedido> allPedidos() {
        try {
            return pgPedidoDAO.all();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
