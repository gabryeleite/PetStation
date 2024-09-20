package com.ecommerce.petstation.controllers;

import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.petstation.dao.PgProdutoDAO;
import com.ecommerce.petstation.models.Produto;

@RestController
@RequestMapping(value="/api")
public class ProdutoController {

    @Autowired
    private PgProdutoDAO pgProdutoDAO;

    @GetMapping(value="/helloworld")
    public String helloworld(){
        return "Hello World";
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value="/produtos")
    public List<Produto> produtos() {
        try {
            return pgProdutoDAO.all();
        } catch (SQLException e) {
            // Gerenciar exceções adequadamente (pode retornar um código de erro apropriado)
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping(value="/produto/{id}")
    public Produto produtoById(@PathVariable(value="id") Integer id) {
        try {
            return pgProdutoDAO.findByIdProduto(id);
        } catch (SQLException e) {
            // Gerenciar exceções adequadamente
            e.printStackTrace();
            return null;
        }
    }
}
