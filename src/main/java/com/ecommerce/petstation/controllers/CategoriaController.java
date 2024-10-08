package com.ecommerce.petstation.controllers;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.petstation.daos.daosPg.PgCategoriaDAO;
import com.ecommerce.petstation.models.Categoria;

@RestController
@RequestMapping(value="/api")
public class CategoriaController {

    @Autowired
    private PgCategoriaDAO pgCategoriaDAO;

    @GetMapping(value = "/categorias")
    public List<Categoria> readCategorias() {
        try {
            return pgCategoriaDAO.all();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping(value = "/categoria/{id}")
    public Categoria CategoriaById(@PathVariable(value="id") Integer id) {
        try {
            return pgCategoriaDAO.getCategoriaById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping(value = "/criar-categoria")
    public ResponseEntity<String> criarCategoria(@RequestBody Categoria categoria) {
        try {
            pgCategoriaDAO.create(categoria);
            return ResponseEntity.ok("Categoria criada com sucesso.");
        } catch (SQLException e) {
            String sqlState = e.getSQLState();

            if (sqlState != null && sqlState.equals("23505")) { // Verifica violação de unicidade
                return ResponseEntity.badRequest().body("Erro: Nome da categoria já existe.");
            } else if (e.getMessage().contains("campos obrigatórios não podem ser nulos")) {
                return ResponseEntity.badRequest().body("Erro: Campos obrigatórios não podem ser nulos.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar categoria.");
            }
        }
    }



    @PostMapping(value="/update-categoria")
    public ResponseEntity<String> updateCategoria(@RequestBody Categoria categoria) {
        try {
            pgCategoriaDAO.update(categoria);
            return ResponseEntity.ok("Categoria editada com sucesso.");
        } catch (SQLException e) {
            if (e.getMessage().contains("nome já existente")) {
                return ResponseEntity.badRequest().body("Erro: Nome do categoria já existe.");
            } else if (e.getMessage().contains("campos obrigatórios não podem ser nulos")) {
                return ResponseEntity.badRequest().body("Erro: Campos obrigatórios não podem ser nulos.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao editar categoria.");
            }
        }
    }

    @DeleteMapping(value="/delete-categoria/{id}")
    public ResponseEntity<String> deleteCategoria(@PathVariable Integer id) throws SQLException {
        try {
            pgCategoriaDAO.delete(id);
            return ResponseEntity.ok("Categoria excluído com sucesso.");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro ao excluir: categoria não encontrado.");
        }
    }

}
