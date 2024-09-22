package com.ecommerce.petstation.controllers;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

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
import com.ecommerce.petstation.daos.daosPg.PgPedidoProdutoDAO;
import com.ecommerce.petstation.daos.daosPg.PgProdutoDAO;
import com.ecommerce.petstation.dtos.PedidoRequestDTO;
import com.ecommerce.petstation.dtos.ProdutoRequestDTO;
import com.ecommerce.petstation.models.Pedido;
import com.ecommerce.petstation.models.Produto;
import com.ecommerce.petstation.models.PedidoProduto;

@RestController
@RequestMapping(value="/api")
public class PedidoController {
    
    @Autowired
    private PgPedidoDAO pgPedidoDAO;

    @Autowired
    private PgPedidoProdutoDAO pgPedidoProdutoDAO;

    @Autowired
    private PgProdutoDAO pgProdutoDAO;

    @GetMapping(value = "/pedido/{nf}")
    public Pedido readPedido(@PathVariable(value="nf") String nf) {
        try {
            return pgPedidoDAO.findByNF(nf);
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

    @GetMapping(value = "/pedidos-cliente/{cpf}")
    public List<Pedido> pedidosCliente(@PathVariable(value="cpf") String cpf) {
        try {
            return pgPedidoDAO.findByCliente(cpf);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/fazer-pedido")
    public ResponseEntity<?> fazerPedido(@RequestBody PedidoRequestDTO requestDTO) {
        try {
            LocalTime localTime = LocalTime.now();
            LocalDate localDate = LocalDate.now();

            String ano = String.valueOf(localDate.getYear()).substring(2);
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String id1 = uuid.substring(0, 3).toUpperCase();
            String id2 = uuid.substring(0, 6).toUpperCase();
            String notaFiscal = ano + "-" + id1 + "-" + id2;

            Pedido pedido = new Pedido();
            pedido.setCpfCliente(requestDTO.getCpfCliente());
            pedido.setNotaFiscal(notaFiscal);
            pedido.setDataPedido(localDate);
            pedido.setHoraPedido(localTime);

            pgPedidoDAO.create(pedido);

            for (ProdutoRequestDTO produtoDTO : requestDTO.getProdutos() ) {

                Produto produto = pgProdutoDAO.read(produtoDTO.getNumProduto());

                if(produtoDTO.getQntComprada() > produto.getEstoque()) {
                    return ResponseEntity.badRequest().body("Quantidade indisponível! Produto: " 
                    + produto.getNome() + " | Estoque: " + produto.getEstoque());
                }

                PedidoProduto pedidoProduto = new PedidoProduto();
                pedidoProduto.setNfPedido(notaFiscal);
                pedidoProduto.setNumProduto(produto.getNum());
                pedidoProduto.setQntProduto(produtoDTO.getQntComprada());
                pedidoProduto.setPrecoProduto(produto.getPreco());

                pgPedidoProdutoDAO.create(pedidoProduto);

                pedido.getItensPedido().add(pedidoProduto);

                Integer novoEstoque = produto.getEstoque() - produtoDTO.getQntComprada();
                pgProdutoDAO.updateEstoque(novoEstoque, produto.getNum());
            }
            return ResponseEntity.ok().body(pedido);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
