package com.ecommerce.petstation.controllers;

import java.sql.SQLException;
import java.util.List;

import com.ecommerce.petstation.dtos.PedidoProdutoResumoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.petstation.daos.daosPg.PgPedidoProdutoDAO;
import com.ecommerce.petstation.dtos.PedidoProdutoDTO;
import com.ecommerce.petstation.models.PedidoProduto;

@RestController
@RequestMapping(value="/api")
public class PedidoProdutoController {

    @Autowired
    private PgPedidoProdutoDAO PgPedidoProdutoDAO;

    @GetMapping(value = "/vendas")
    public List<PedidoProduto> allVendas() {
        try {
            return PgPedidoProdutoDAO.all();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping(value="/criar-venda")
    public ResponseEntity<String> criarPedido(@RequestBody PedidoProduto venda) {
        try {
            PgPedidoProdutoDAO.create(venda);
            return ResponseEntity.ok("Venda criada com sucesso.");
        } catch (SQLException e) {
            if (e.getMessage().contains("campos obrigatórios não podem ser nulos")) {
                return ResponseEntity.badRequest().body("Erro: Campos obrigatórios não podem ser nulos.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar venda.");
            }
        }
    }

    @GetMapping(value = "/vendas-total")
    public List<PedidoProdutoDTO> allVendasTotal() {
        try {
            return PgPedidoProdutoDAO.filtrarVendas();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping(value = "/resumo-vendas")
    public List<PedidoProdutoResumoDTO> resumirVendas() {
        try {
            return PgPedidoProdutoDAO.resumoVendas();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
