package com.ecommerce.petstation.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ecommerce.petstation.models.Subcategoria;

@Repository
public class PgSubcategoriaDAO implements SubcategoriaDAO {

    private final Connection connection;

    public PgSubcategoriaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Subcategoria subcategoria) throws SQLException {
        String sql = "INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES(?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, subcategoria.getNome());
            stmt.setInt(2, subcategoria.getIdCategoria());
            stmt.executeUpdate();
        }
    }

    @Override
    public Subcategoria read(Integer id) throws SQLException {
        String sql = "SELECT * FROM petstation.subcategoria WHERE id_subcategoria = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Subcategoria subcategoria = new Subcategoria();
                subcategoria.setNome(rs.getString("nome"));
                subcategoria.setIdCategoria(rs.getInt("id_categoria"));
                return subcategoria;
            } else {
                return null;
            }
        }
    }

    @Override
    public void update(Subcategoria subcategoria) throws SQLException {
        String sql = "UPDATE petstation.subcategoria SET nome = ?, SET id_categoria = ? WHERE id_subcategoria = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, subcategoria.getNome());
            stmt.setInt(2, subcategoria.getIdCategoria());
            stmt.setInt(3, subcategoria.getIdSubcategoria());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM petstation.subcategoria WHERE id_subcategoria = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Subcategoria> all() throws SQLException {
        String sql = "SELECT * FROM petstation.subcategoria";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            List<Subcategoria> categorias = new ArrayList<>();

            while (rs.next()) {
                Subcategoria subcategoria = new Subcategoria();
                subcategoria.setNome(rs.getString("nome"));
                categorias.add(subcategoria);
            }

            return categorias;
        }
    }

    public List<Subcategoria> findByCategoria(Integer idCategoria) throws SQLException {
        String sql = "SELECT * FROM petstation.subcategoria WHERE id_categoria = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCategoria);
            ResultSet rs = stmt.executeQuery();
            List<Subcategoria> categorias = new ArrayList<>();

            while (rs.next()) {
                Subcategoria subcategoria = new Subcategoria();
                subcategoria.setNome(rs.getString("nome"));
                categorias.add(subcategoria);
            }

            return categorias;
        }
    }

}
