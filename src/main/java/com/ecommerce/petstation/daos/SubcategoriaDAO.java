package com.ecommerce.petstation.daos;

import java.sql.SQLException;
import java.util.List;

import com.ecommerce.petstation.models.Subcategoria;

public interface SubcategoriaDAO extends DAO<Subcategoria> {
    List<Subcategoria> findByCategoria(Integer idCategoria) throws SQLException;
}
