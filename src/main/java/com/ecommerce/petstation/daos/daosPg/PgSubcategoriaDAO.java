package com.ecommerce.petstation.daos.daosPg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ecommerce.petstation.dtos.SubcategoriaDTO;
import org.springframework.stereotype.Repository;

import com.ecommerce.petstation.daos.SubcategoriaDAO;
import com.ecommerce.petstation.models.Subcategoria;

@Repository
public class PgSubcategoriaDAO implements SubcategoriaDAO {

    private final Connection connection;

    private static final Logger LOGGER = Logger.getLogger(PgSubcategoriaDAO.class.getName());

    public PgSubcategoriaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Subcategoria subcategoria) throws SQLException {
        String sql = "INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES(?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, subcategoria.getNome());
            stmt.setInt(2, subcategoria.getIdCategoria());

            if (stmt.executeUpdate() < 1) {
                throw new SQLException("Erro ao criar subcategoria: inserção falhou.");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao criar subcategoria.", ex);
            if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao criar subcategoria: campos obrigatórios não podem ser nulos.");
            } else {
                throw new SQLException("Erro ao criar subcategoria.");
            }
        }
    }

    @Override
    public Subcategoria read(Integer id) throws SQLException {
        String sql = "SELECT * FROM petstation.subcategoria WHERE id_subcategoria = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Subcategoria subcategoria = new Subcategoria();
                    subcategoria.setIdSubcategoria(rs.getInt("id_subcategoria"));
                    subcategoria.setNome(rs.getString("nome"));
                    subcategoria.setIdCategoria(rs.getInt("id_categoria"));
                    return subcategoria;
                } else {
                    throw new SQLException("Erro ao visualizar: subcategoria não encontrada.");
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao ler subcategoria.", ex);
            if (ex.getMessage().equals("Erro ao visualizar: subcategoria não encontrada.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao ler subcategoria.");
            }
        }
    }

    @Override
    public void update(Subcategoria subcategoria) throws SQLException {
        String sql = "UPDATE petstation.subcategoria SET nome = ?, id_categoria = ? WHERE id_subcategoria = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, subcategoria.getNome());
            stmt.setInt(2, subcategoria.getIdCategoria());
            stmt.setInt(3, subcategoria.getIdSubcategoria());

            if (stmt.executeUpdate() < 1) {
                throw new SQLException("Erro ao editar: subcategoria não encontrada.");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar subcategoria.", ex);
            if (ex.getMessage().equals("Erro ao editar: subcategoria não encontrada.")) {
                throw ex;
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao editar subcategoria: campos obrigatórios não podem ser nulos.");
            } else {
                throw new SQLException("Erro ao editar subcategoria.");
            }
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM petstation.subcategoria WHERE id_subcategoria = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            if (stmt.executeUpdate() < 1) {
                throw new SQLException("Erro ao excluir: subcategoria não encontrada.");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao excluir subcategoria.", ex);
            if (ex.getMessage().equals("Erro ao excluir: subcategoria não encontrada.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao excluir subcategoria.");
            }
        }
    }

    @Override
    public List<Subcategoria> all() throws SQLException {
        String sql = "SELECT * FROM petstation.subcategoria";
        List<Subcategoria> subcategorias = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Subcategoria subcategoria = new Subcategoria();
                subcategoria.setIdSubcategoria(rs.getInt("id_subcategoria"));
                subcategoria.setNome(rs.getString("nome"));
                subcategoria.setIdCategoria(rs.getInt("id_categoria"));
                subcategorias.add(subcategoria);
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao listar subcategorias.", ex);
            throw new SQLException("Erro ao listar subcategorias.");
        }
        return subcategorias;
    }

    @Override
    public List<SubcategoriaDTO> Listar() throws SQLException {
        String sql = "SELECT s.*, c.nome AS nomeCategoria " +
                "FROM petstation.subcategoria AS s " +
                "LEFT JOIN petstation.categoria AS c ON s.id_categoria = c.id_categoria; ";
        List<SubcategoriaDTO> subcategoriasDTO = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                SubcategoriaDTO subcategoriaDTO = new SubcategoriaDTO();
                subcategoriaDTO.setIdSubcategoria(rs.getInt("id_subcategoria"));
                subcategoriaDTO.setNome(rs.getString("nome"));
                subcategoriaDTO.setIdCategoria(rs.getInt("id_categoria"));
                subcategoriaDTO.setNomeCategoria(rs.getString("nomeCategoria"));
                subcategoriasDTO.add(subcategoriaDTO);
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao listar subcategorias.", ex);
            throw new SQLException("Erro ao listar subcategorias.");
        }
        return subcategoriasDTO;
    }

    public List<Subcategoria> findByCategoria(Integer idCategoria) throws SQLException {
        String sql = "SELECT * FROM petstation.subcategoria WHERE id_categoria = ?";
        List<Subcategoria> subcategorias = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCategoria);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Subcategoria subcategoria = new Subcategoria();
                    subcategoria.setIdSubcategoria(rs.getInt("id_subcategoria"));
                    subcategoria.setNome(rs.getString("nome"));
                    subcategoria.setIdCategoria(rs.getInt("id_categoria"));
                    subcategorias.add(subcategoria);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar subcategorias por categoria.", ex);
            throw new SQLException("Erro ao buscar subcategorias por categoria.");
        }
        return subcategorias;
    }
}
