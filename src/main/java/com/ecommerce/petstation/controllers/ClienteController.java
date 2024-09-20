package com.ecommerce.petstation.controllers;

import java.sql.SQLException;
import java.util.List;

import com.ecommerce.petstation.models.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.petstation.dao.PgClienteDAO;
import com.ecommerce.petstation.models.Cliente;

@RestController
@RequestMapping(value="/api")
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

    @GetMapping(value = "/cliente-id/{id}")
    public Cliente clienteById(@PathVariable(value="id") Integer id) {
        try {
            return pgClienteDAO.findByIdCliente(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping(value = "/cliente-cpf/{cpf}")
    public Cliente clienteByCpf(@PathVariable(value="cpf") String cpf) {
        try {
            return pgClienteDAO.findByCpfCliente(cpf);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    @PostMapping(value="/criar-cliente")
    public ResponseEntity<String> criarProduto(@RequestBody Produto cliente) {
        try {
            pgClienteDAO.create(cliente);
            return ResponseEntity.ok("Produto criado com sucesso.");
        } catch (SQLException e) {
            if (e.getMessage().contains("subcategoria associada não existe")) {
                return ResponseEntity.badRequest().body("Erro: Subcategoria associada não existe.");
            } else if (e.getMessage().contains("nome já existente")) {
                return ResponseEntity.badRequest().body("Erro: Nome do cliente já existe.");
            } else if (e.getMessage().contains("campos obrigatórios não podem ser nulos")) {
                return ResponseEntity.badRequest().body("Erro: Campos obrigatórios não podem ser nulos.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar cliente.");
            }
        }
    }   */

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
