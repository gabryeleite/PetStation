package com.ecommerce.petstation.controllers;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.petstation.dao.PgSubcategoriaDAO;
import com.ecommerce.petstation.models.Subcategoria;

@RestController
@RequestMapping(value="/api")
public class SubcategoriaController {

    @Autowired
    private PgSubcategoriaDAO pgSubcategoriaDAO;

    @GetMapping(value = "/subcategorias")
    public List<Subcategoria> readSubcategorias() {
        try {
            return pgSubcategoriaDAO.all();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping(value = "/criar-subcategoria")
    public ResponseEntity<String> criarSubcategoria(@RequestBody Subcategoria subcategoria) {
        try {
            pgSubcategoriaDAO.create(subcategoria);
            return ResponseEntity.ok("Subcategoria criada com sucesso.");
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                return ResponseEntity.badRequest().body("Erro: Nome da subcategoria já existe.");
            } else if (e.getMessage().contains("campos obrigatórios não podem ser nulos")) {
                return ResponseEntity.badRequest().body("Erro: Campos obrigatórios não podem ser nulos.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar subcategoria.");
            }
        }
    }


    @PostMapping(value="/update-subcategoria")
    public ResponseEntity<String> updateSubcategoria(@RequestBody Subcategoria subcategoria) {
        try {
            pgSubcategoriaDAO.update(subcategoria);
            return ResponseEntity.ok("Subcategoria editada com sucesso.");
        } catch (SQLException e) {
            if (e.getMessage().contains("nome já existente")) {
                return ResponseEntity.badRequest().body("Erro: Nome do subcategoria já existe.");
            } else if (e.getMessage().contains("campos obrigatórios não podem ser nulos")) {
                return ResponseEntity.badRequest().body("Erro: Campos obrigatórios não podem ser nulos.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao editar subcategoria.");
            }
        }
    }

    @DeleteMapping(value="/delete-subcategoria/{id}")
    public ResponseEntity<String> deleteSubcategoria(@PathVariable Integer id) throws SQLException {
        try {
            pgSubcategoriaDAO.delete(id);
            return ResponseEntity.ok("Subcategoria excluído com sucesso.");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro ao excluir: subcategoria não encontrado.");
        }
    }

    @GetMapping(value = "/subcategoria-categoria/{id}")
    public List<Subcategoria> subcategoriaByCategoria(@PathVariable(value="id") Integer id) {
        try {
            return pgSubcategoriaDAO.findByCategoria(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
