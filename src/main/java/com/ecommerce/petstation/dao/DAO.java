package com.ecommerce.petstation.dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
    // Para abstrair e encapsular todo o acesso à fonte de dados, gerenciamento de conexão
    void create(T t) throws SQLException;
    T read(Integer id) throws SQLException;
    void update(T t) throws SQLException;
    void delete(Integer id) throws SQLException;
    List<T> all() throws SQLException;  
    
}