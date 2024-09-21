package com.ecommerce.petstation.controllers;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping(value="/criar-pedido")
    public ResponseEntity<String> criarPedido(@RequestBody Pedido pedido) {
        try {
            pgPedidoDAO.create(pedido);
            return ResponseEntity.ok("Pedido criado com sucesso.");
        } catch (SQLException e) {
            if (e.getMessage().contains("campos obrigatórios não podem ser nulos")) {
                return ResponseEntity.badRequest().body("Erro: Campos obrigatórios não podem ser nulos.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar pedido.");
            }
        }
    }

    @GetMapping(value = "/pedidos-cliente/{id}")
    public List<Pedido> pedidosCliente(@PathVariable(value="id") Integer id) {
        try {
            return pgPedidoDAO.findByIdCliente(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
