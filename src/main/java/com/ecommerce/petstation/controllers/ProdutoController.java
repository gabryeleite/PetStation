package com.ecommerce.petstation.controllers;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.petstation.daos.daosPg.PgProdutoDAO;
import com.ecommerce.petstation.dtos.ProdutoDTO;
import com.ecommerce.petstation.models.Produto;

@RestController
@RequestMapping(value="/api")
public class ProdutoController {

    @Autowired
    private PgProdutoDAO pgProdutoDAO;

    // Vou deixar isso para teste de conexao do banco
    @GetMapping(value="/helloworld")
    public String helloworld(){
        return "Hello World";
    }


    //@CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value="/produtos")
    public List<Produto> produtos() {
        try {
            return pgProdutoDAO.all();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping(value="/produto/{id}")
    public Produto produtoById(@PathVariable(value="id") Integer id) {
        try {
            return pgProdutoDAO.read(id);
        } catch (SQLException e) {
            // Gerenciar exceções adequadamente
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping(value="/criar-produto")
    public ResponseEntity<String> criarProduto(@RequestBody Produto produto) {
        try {
            pgProdutoDAO.create(produto);
            return ResponseEntity.ok("Produto criado com sucesso.");
        } catch (SQLException e) {
            if (e.getMessage().contains("produto associada não existe")) {
                return ResponseEntity.badRequest().body("Erro: Produto associada não existe.");
            } else if (e.getMessage().contains("nome já existente")) {
                return ResponseEntity.badRequest().body("Erro: Nome do produto já existe.");
            } else if (e.getMessage().contains("campos obrigatórios não podem ser nulos")) {
                return ResponseEntity.badRequest().body("Erro: Campos obrigatórios não podem ser nulos.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar produto.");
            }
        }
    }

    @DeleteMapping(value="/delete-produto/{id}")
    public ResponseEntity<String> deleteProduto(@PathVariable Integer id) throws SQLException {
        try {
            pgProdutoDAO.delete(id);
            return ResponseEntity.ok("Produto excluído com sucesso.");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro ao excluir: produto não encontrado.");
        }
    }

    @GetMapping(value="/produtos-categoria/{id}")
    public List<Produto> produtosCategoria(@PathVariable("id") Integer idCategoria) {
        try {
            return pgProdutoDAO.findByCategoria(idCategoria);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping(value="/produtos-cliente/{cpf}")
    public List<ProdutoDTO> produtosCliente(@PathVariable("cpf") String cpfCliente) {
        try {
            return pgProdutoDAO.findByCliente(cpfCliente);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping(value="/atualizar-estoque/{id}")
    public ResponseEntity<String> updateEstoque(@PathVariable(value="id") Integer id, @RequestBody(required=true) Map<String, Integer> body) {
        Integer novoEstoque = body.get("novoEstoque");
        if (novoEstoque == null) {
            return ResponseEntity.badRequest().body("O campo 'novoEstoque' é obrigatório.");
        }

        try {
            pgProdutoDAO.updateEstoque(novoEstoque, id);
            return ResponseEntity.ok("Estoque atualizado com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar o estoque.");
        }
    }

    @GetMapping("/mais-vendidos")
    public ResponseEntity<List<Produto>> getMaisVendidos() {
        try {
            List<Produto> produtos = pgProdutoDAO.findMaisVendidos();
            return ResponseEntity.ok(produtos);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @GetMapping(value="/produtos-termo/{termo}")
    public List<Produto> produtosTermo(@PathVariable("termo") String termo) {
        try {
            return pgProdutoDAO.findByTermo(termo);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
