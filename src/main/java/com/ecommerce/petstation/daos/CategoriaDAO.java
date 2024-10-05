package com.ecommerce.petstation.daos;

import com.ecommerce.petstation.models.Categoria;

import java.sql.SQLException;

public interface CategoriaDAO extends DAO<Categoria> {
    Categoria getCategoriaById(Integer id_categoria) throws SQLException;
}
