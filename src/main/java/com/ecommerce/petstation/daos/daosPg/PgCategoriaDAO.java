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
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class PgCategoriaDAO implements CategoriaDAO {

    private final Connection connection;
    
    private static final Logger LOGGER = Logger.getLogger(PgCategoriaDAO.class.getName());

    public PgCategoriaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO petstation.categoria (nome) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao criar categoria.", ex);
            if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao criar categoria: campos obrigatórios não podem ser nulos.");
            } else {
                throw new SQLException("Erro ao criar categoria.");
            }
        }
    }


    @Override
    public Categoria read(Integer id) throws SQLException {
        String sql = "SELECT * FROM petstation.categoria WHERE id_categoria = ?";
        Categoria categoria = new Categoria();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                categoria.setIdCategoria(id);
                categoria.setNome(rs.getString("nome"));
            } else {
                throw new SQLException("Erro ao visualizar: categoria não encontrada.");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao ler categoria.", ex);
            if (ex.getMessage().equals("Erro ao visualizar: categoria não encontrada.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao ler categoria.");
            }
        }
        return categoria;
    }

    @Override
    public void update(Categoria categoria) throws SQLException {
        String sql = "UPDATE petstation.categoria SET nome = ? WHERE id_categoria = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.setInt(2, categoria.getIdCategoria());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar categoria.", ex);
            if (ex.getMessage().equals("Erro ao editar: categoria não encontrada.")) {
                throw ex;
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao editar categoria: campos obrigatórios não podem ser nulos.");
            } else {
                throw new SQLException("Erro ao editar categoria.");
            }
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM petstation.categoria WHERE id_categoria = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao excluir categoria.", ex);
            if (ex.getMessage().equals("Erro ao excluir: categoria não encontrada.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao excluir categoria.");
            }
        }
    }

    @Override
    public List<Categoria> all() throws SQLException {
        String sql = "SELECT * FROM petstation.categoria ORDER BY id_categoria";
        List<Categoria> categorias = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("id_categoria"));
                categoria.setNome(rs.getString("nome"));
                categorias.add(categoria);
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao listar categorias.", ex);
            throw new SQLException("Erro ao listar categorias.");
        }
        return categorias;
    }
    @Override
    public Categoria getCategoriaById(Integer id_categoria) throws SQLException {
        String sql = "SELECT * FROM petstation.categoria WHERE id_categoria = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_categoria);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("id_categoria"));
                categoria.setNome(rs.getString("nome"));
                return categoria;
            } else {
                return null;
            }
        }
    }

}