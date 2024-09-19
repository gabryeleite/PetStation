package com.ecommerce.petstation.dao;

import java.sql.SQLException;
import com.ecommerce.petstation.models.Categoria;
//import java.util.List;

public interface CategoriaDAO extends DAO<Categoria> {
    void delete(Integer id) throws SQLException;

    public Categoria getCategoriaByNome(String nome) throws SQLException;
}
