package com.ecommerce.petstation.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.petstation.models.Produto;
import com.ecommerce.petstation.repositories.ProdutoRepository;

@RestController
@RequestMapping(value="/api")
public class ProdutoController {
    
    @Autowired
    ProdutoRepository produtoRepository;

    @GetMapping(value="/helloworld")
    public String helloworld(){
        return "Hello World";
    };

    @GetMapping(value="/produtos")
    public List<Produto> produtos(){
        return produtoRepository.findAll();
    };

    @GetMapping(value="/produto/{id}")
    public Produto produtoById(@PathVariable(value="id") Integer id){
        return produtoRepository.findByIdProduto(id);
    };

}
