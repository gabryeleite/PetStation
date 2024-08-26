package com.ecommerce.petstation.services;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.petstation.models.Produto;
import com.ecommerce.petstation.models.Subcategoria;
import com.ecommerce.petstation.repositories.ProdutoRepository;
import com.ecommerce.petstation.repositories.SubcategoriaRepository;

@Service
public class ProdutoService {
    
    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    SubcategoriaRepository subcategoriaRepository;

    public Produto findById(Integer num) {
        Optional<Produto> produto = this.produtoRepository.findById(num);
        return produto.orElseThrow(() -> new RuntimeException("Produto nao encontrado! id:" + num));
    }

    public void createProduto(String nome, BigDecimal preco, String descricao, 
        Integer estoque, Subcategoria subcategoria) throws Exception {
        produtoRepository.createProduto(nome, preco, descricao, estoque, subcategoria.getIdSubcategoria());
    }

    public List<Produto> findProdutoBySubcategoria(Integer idSubcategoria) throws Exception {
        Optional<Subcategoria> subcategoria = subcategoriaRepository.findById(idSubcategoria);

        if(subcategoria.isEmpty()) {
            throw new Exception("Subcategoria nao encontrada!");
        }

        return produtoRepository.findBySubcategoria(idSubcategoria);
    }

    //@Transactional
    public void deleteProduto(Integer num) {
        findById(num);
        try {
            this.produtoRepository.deleteById(num);
        } catch (Exception e) {
            throw new RuntimeException("Nao eh possivel deletar produto, relacoes envolvidas!");
        }
    }

}
