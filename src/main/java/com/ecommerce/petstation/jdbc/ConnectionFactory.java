package com.ecommerce.petstation.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public abstract class ConnectionFactory {

    private static ConnectionFactory instance = null;
    protected static String propertiesPath = "../../conf/datasource.properties";
    private static String dbServer;
    
    protected ConnectionFactory() {
    }

    public static ConnectionFactory getInstance() throws IOException {
        if (instance == null) {
            Properties properties = new Properties();

            try {
                InputStream input = ConnectionFactory.class.getClassLoader().getResourceAsStream(propertiesPath);
                properties.load(input);

                dbServer = properties.getProperty("server");
            } catch (IOException ex) {
                System.err.println(ex.getMessage());

                throw new IOException("Erro ao obter informações do banco de dados.");
            }

            if (getDbServer().equals("postgresql")) {
                instance = new PgConnectionFactory();
            }
            else {
                throw new RuntimeException("Servidor de banco de dados não suportado.");
            }
        }

        return instance;
    }
    
    public static String getDbServer() {
        return dbServer;
    }

    public abstract Connection getConnection() throws IOException, SQLException, ClassNotFoundException;
    
}