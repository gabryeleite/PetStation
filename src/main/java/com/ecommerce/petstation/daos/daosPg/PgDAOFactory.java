package com.ecommerce.petstation.daos.daosPg;

import java.sql.Connection;

import com.ecommerce.petstation.daos.DAOFactory;
import com.ecommerce.petstation.daos.ProdutoDAO;

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