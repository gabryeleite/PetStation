package com.ecommerce.petstation.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Repository;

import com.ecommerce.petstation.models.Produto;

@Repository
public class PgProdutoDAO implements ProdutoDAO {

    private final Connection connection;
    private static final Logger LOGGER = Logger.getLogger(PgProdutoDAO.class.getName());

    private static final String CREATE_QUERY =
            "INSERT INTO petstation.produto (nome, preco, descricao, estoque, id_subcategoria) " +
            "VALUES (?, ?, ?, ?, ?);";

    private static final String READ_QUERY =
            "SELECT nome, preco, descricao, estoque, id_subcategoria FROM petstation.produto WHERE num = ?;";

    private static final String UPDATE_QUERY =
            "UPDATE petstation.produto SET nome = ?, preco = ?, descricao = ?, estoque = ?, id_subcategoria = ? WHERE num = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM petstation.produto WHERE num = ?;";

    private static final String ALL_QUERY =
            "SELECT num, nome, preco, descricao, estoque, id_subcategoria FROM petstation.produto ORDER BY num DESC;";

    private static final String FIND_BY_ID_PRODUTO_QUERY =
            "SELECT * FROM petstation.produto WHERE num = ?;";

    private static final String FIND_BY_SUBCATEGORIA_QUERY =
            "SELECT * FROM petstation.produto WHERE id_subcategoria = ?;";

    private static final String UPDATE_ESTOQUE_QUERY =
            "UPDATE petstation.produto SET estoque = ? WHERE num = ?;";

    public PgProdutoDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Produto produto) throws SQLException {
        // Pode-se reutilizar createProduto
        //createProduto(produto.getNome(), produto.getPreco(), produto.getDescricao(), produto.getEstoque(), produto.getSubcategoria().getIdSubcategoria());
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, produto.getNome());
            statement.setBigDecimal(2, produto.getPreco());
            statement.setString(3, produto.getDescricao());
            statement.setInt(4, produto.getEstoque());
            statement.setInt(5, produto.getIdSubcategoria());
            statement.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao inserir produto.", ex);
            if (ex.getMessage().contains("fk_produto_subcategoria")) {
                throw new SQLException("Erro ao inserir produto: subcategoria associada não existe.");
            } else if (ex.getMessage().contains("uq_produto_nome")) {
                throw new SQLException("Erro ao inserir produto: nome já existente.");
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao inserir produto: campos obrigatórios não podem ser nulos.");
            } else {
                throw new SQLException("Erro ao inserir produto.");
            }
        }
    }

    @Override
    public Produto read(Integer id) throws SQLException {
        Produto produto = new Produto();
        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    produto.setNum(id);
                    produto.setNome(result.getString("nome"));
                    produto.setPreco(result.getBigDecimal("preco"));
                    produto.setDescricao(result.getString("descricao"));
                    produto.setEstoque(result.getInt("estoque"));
                    produto.setIdSubcategoria(result.getInt("id_subcategoria"));
                    
                } else {
                    throw new SQLException("Erro ao visualizar: produto não encontrado.");
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao ler produto.", ex);
            if (ex.getMessage().equals("Erro ao visualizar: produto não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao ler produto.");
            }
        }
        return produto;
    }

    @Override
    public void update(Produto produto) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, produto.getNome());
            statement.setBigDecimal(2, produto.getPreco());
            statement.setString(3, produto.getDescricao());
            statement.setInt(4, produto.getEstoque());
            statement.setInt(5, produto.getIdSubcategoria());
            statement.setInt(6, produto.getNum());

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao editar: produto não encontrado.");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar produto.", ex);
            if (ex.getMessage().equals("Erro ao editar: produto não encontrado.")) {
                throw ex;
            } else if (ex.getMessage().contains("fk_produto_subcategoria")) { // Supondo uma FK
                throw new SQLException("Erro ao editar produto: subcategoria associada não existe.");
            } else if (ex.getMessage().contains("uq_produto_nome")) { // Supondo constraint de unicidade
                throw new SQLException("Erro ao editar produto: nome já existente.");
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao editar produto: campos obrigatórios não podem ser nulos.");
            } else {
                throw new SQLException("Erro ao editar produto.");
            }
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao excluir: produto não encontrado.");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao excluir produto.", ex);
            if (ex.getMessage().equals("Erro ao excluir: produto não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao excluir produto.");
            }
        }
    }

    @Override
    public List<Produto> all() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
            ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Produto produto = new Produto();
                produto.setNum(result.getInt("num"));
                produto.setNome(result.getString("nome"));
                produto.setPreco(result.getBigDecimal("preco"));
                produto.setDescricao(result.getString("descricao"));
                produto.setEstoque(result.getInt("estoque"));
                produto.setIdSubcategoria(result.getInt("id_subcategoria"));

                produtos.add(produto);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao listar produtos.", ex);
            throw new SQLException("Erro ao listar produtos.");
        }
        return produtos;
    }

    @Override
    public Produto findByIdProduto(Integer num) throws SQLException {
        Produto produto = null;
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_PRODUTO_QUERY)) {
            statement.setInt(1, num);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    produto = new Produto();
                    produto.setNum(result.getInt("num"));
                    produto.setNome(result.getString("nome"));
                    produto.setPreco(result.getBigDecimal("preco"));
                    produto.setDescricao(result.getString("descricao"));
                    produto.setEstoque(result.getInt("estoque"));
                    produto.setIdSubcategoria(result.getInt("id_subcategoria"));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar produto por ID.", ex);
            throw new SQLException("Erro ao buscar produto por ID.");
        }
        return produto;
    }

    @Override
    public List<Produto> findBySubcategoria(Integer subcategoria) throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_SUBCATEGORIA_QUERY)) {
            statement.setInt(1, subcategoria);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Produto produto = new Produto();
                    produto.setNum(result.getInt("num"));
                    produto.setNome(result.getString("nome"));
                    produto.setPreco(result.getBigDecimal("preco"));
                    produto.setDescricao(result.getString("descricao"));
                    produto.setEstoque(result.getInt("estoque"));
                    produto.setIdSubcategoria(result.getInt("id_subcategoria"));
                    produtos.add(produto);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar produtos por subcategoria.", ex);
            throw new SQLException("Erro ao buscar produtos por subcategoria.");
        }
        return produtos;
    }

    /* 
    @Override
    public void createProduto(String nome, BigDecimal preco, String descricao, Integer estoque, Integer idSubcategoria) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, nome);
            statement.setBigDecimal(2, preco);
            statement.setString(3, descricao);
            statement.setInt(4, estoque);
            statement.setInt(5, idSubcategoria);
            statement.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao inserir produto.", ex);
            if (ex.getMessage().contains("fk_produto_subcategoria")) {
                throw new SQLException("Erro ao inserir produto: subcategoria associada não existe.");
            } else if (ex.getMessage().contains("uq_produto_nome")) {
                throw new SQLException("Erro ao inserir produto: nome já existente.");
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao inserir produto: campos obrigatórios não podem ser nulos.");
            } else {
                throw new SQLException("Erro ao inserir produto.");
            }
        }
    }
    */

    @Override
    public void updateEstoque(Integer novoEstoque, Integer num) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_ESTOQUE_QUERY)) {
            statement.setInt(1, novoEstoque);
            statement.setInt(2, num);
            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao atualizar estoque: produto não encontrado.");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar estoque do produto.", ex);
            if (ex.getMessage().equals("Erro ao atualizar estoque: produto não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao atualizar estoque do produto.");
            }
        }
    }
}
