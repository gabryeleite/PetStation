package com.ecommerce.petstation.daos.daosPg;

import org.springframework.stereotype.Repository;

import com.ecommerce.petstation.daos.CategoriaDAO;
import com.ecommerce.petstation.models.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PgCategoriaDAO implements CategoriaDAO {
    private final Connection connection;

    public PgCategoriaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO petstation.categoria (nome) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }


    @Override
    public Categoria read(Integer id) throws SQLException {
        String sql = "SELECT * FROM petstation.categoria WHERE id_categoria = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setNome(rs.getString("nome"));
                return categoria;
            } else {
                return null;
            }
        }
    }

    @Override
    public void update(Categoria categoria) throws SQLException {
        String sql = "UPDATE petstation.categoria SET nome = ? WHERE id_categoria = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.setInt(2, categoria.getIdCategoria());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM petstation.categoria WHERE id_categoria = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Categoria> all() throws SQLException {
        String sql = "SELECT * FROM petstation.categoria order by id_categoria";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            List<Categoria> categorias = new ArrayList<>();

            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("id_categoria"));
                categoria.setNome(rs.getString("nome"));
                categorias.add(categoria);
            }

            return categorias;
        }
    }
/*
    @Override
    public Categoria getCategoriaByNome(String nome) throws SQLException {
        String sql = "SELECT * FROM petstation.categoria WHERE nome = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("id"));
                categoria.setNome(rs.getString("nome"));
                return categoria;
            } else {
                return null;
            }
        }
    } */

}