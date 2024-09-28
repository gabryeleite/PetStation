package com.ecommerce.petstation.daos;

import com.ecommerce.petstation.models.Categoria;
import com.ecommerce.petstation.models.Subcategoria;

import java.sql.SQLException;
import java.util.List;

public interface CategoriaDAO extends DAO<Categoria> {
    Categoria getCategoriaById(Integer id_categoria) throws SQLException;
}
