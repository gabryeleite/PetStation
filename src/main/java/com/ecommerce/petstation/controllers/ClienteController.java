package com.ecommerce.petstation.controllers;

import java.sql.SQLException;
import java.util.List;

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
    public List<Cliente> readClientes() {
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


    @PostMapping(value="/criar-cliente")
    public ResponseEntity<String> criarCliente(@RequestBody Cliente cliente) {
        try {
            pgClienteDAO.create(cliente);
            return ResponseEntity.ok("Cliente criado com sucesso.");
        } catch (SQLException e) {
            if (e.getMessage().contains("nome já existente")) {
                return ResponseEntity.badRequest().body("Erro: Nome do cliente já existe.");
            } else if (e.getMessage().contains("campos obrigatórios não podem ser nulos")) {
                return ResponseEntity.badRequest().body("Erro: Campos obrigatórios não podem ser nulos.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar cliente.");
            }
        }
    }

    @PostMapping(value="/update-cliente")
    public ResponseEntity<String> updateCliente(@RequestBody Cliente cliente) {
        try {
            pgClienteDAO.update(cliente);
            return ResponseEntity.ok("Cliente criado com sucesso.");
        } catch (SQLException e) {
            if (e.getMessage().contains("nome já existente")) {
                return ResponseEntity.badRequest().body("Erro: Nome do cliente já existe.");
            } else if (e.getMessage().contains("campos obrigatórios não podem ser nulos")) {
                return ResponseEntity.badRequest().body("Erro: Campos obrigatórios não podem ser nulos.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar cliente.");
            }
        }
    }

    @PostMapping(value="/update-cliente")
    public ResponseEntity<String> updateCliente(@RequestBody Cliente cliente) {
        try {
            pgClienteDAO.update(cliente);
            return ResponseEntity.ok("Cliente criado com sucesso.");
        } catch (SQLException e) {
            if (e.getMessage().contains("nome já existente")) {
                return ResponseEntity.badRequest().body("Erro: Nome do cliente já existe.");
            } else if (e.getMessage().contains("campos obrigatórios não podem ser nulos")) {
                return ResponseEntity.badRequest().body("Erro: Campos obrigatórios não podem ser nulos.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar cliente.");
            }
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
