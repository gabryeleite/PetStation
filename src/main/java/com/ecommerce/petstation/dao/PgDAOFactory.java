package com.ecommerce.petstation.dao;

import java.sql.Connection;

// ProdutoDAO
public class PgDAOFactory extends DAOFactory{

    public PgDAOFactory(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ProdutoDAO geProdutoDAO() {
        return new PgProdutoDAO(this.connection);
    }
}