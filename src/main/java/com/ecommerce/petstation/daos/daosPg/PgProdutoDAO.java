package com.ecommerce.petstation.daos.daosPg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Repository;

import com.ecommerce.petstation.daos.ProdutoDAO;
import com.ecommerce.petstation.dtos.ProdutoDTO;
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

    private static final String FIND_BY_CATEGORIA_QUERY =
            "SELECT p.num, p.nome, p.preco, p.descricao, p.estoque, p.id_subcategoria, s.nome AS nome_subcategoria, c.nome AS nome_categoria " +
            "FROM petstation.produto p " +
            "JOIN petstation.subcategoria s ON p.id_subcategoria = s.id_subcategoria " +
            "JOIN petstation.categoria c ON s.id_categoria = c.id_categoria " +
            "WHERE c.id_categoria = ?";

    private static final String FIND_BY_CLIENTE_QUERY =
            "SELECT p.nome AS Produto, c.qnt_produto AS Quantidade, ped.nota_fiscal AS NF_Pedido, " +
            "ped.data_pedido AS Data_Pedido " +
            "FROM petstation.produto p " +
            "JOIN petstation.pedido_produto c ON p.num = c.num_produto " +
            "JOIN petstation.pedido ped ON c.nf_pedido = ped.nota_fiscal " +
            "JOIN petstation.cliente cli ON ped.cpf_cliente = cli.cpf " +
            "WHERE cli.cpf = ?";

    private static final String UPDATE_ESTOQUE_QUERY =
            "UPDATE petstation.produto SET estoque = ? WHERE num = ?;";

    private static final String FIND_MAIS_VENDIDOS_QUERY =
            "SELECT p.num, p.nome, p.preco, p.descricao, p.estoque, p.id_subcategoria, SUM(pp.qnt_produto) AS total_vendido " +
                    "FROM petstation.produto p " +
                    "JOIN petstation.pedido_produto pp ON p.num = pp.num_produto " +
                    "GROUP BY p.num, p.nome, p.preco, p.descricao, p.estoque, p.id_subcategoria " +
                    "ORDER BY total_vendido DESC " +
                    "LIMIT 5;";

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
    public List<Produto> findByCategoria(Integer idCategoria)throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_CATEGORIA_QUERY)) {
            statement.setInt(1, idCategoria);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Produto produto = new Produto();
                    produto.setNum(rs.getInt("num"));
                    produto.setNome(rs.getString("nome"));
                    produto.setPreco(rs.getBigDecimal("preco"));
                    produto.setDescricao(rs.getString("descricao"));
                    produto.setEstoque(rs.getInt("estoque"));
                    produto.setIdSubcategoria(rs.getInt("id_subcategoria"));
    
                    produtos.add(produto);
                }
            }
        }
        return produtos;
    }

    @Override
    public List<ProdutoDTO> findByCliente(String cpfCliente)throws SQLException {
        List<ProdutoDTO> produtosPedidos = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_CLIENTE_QUERY)) {
            statement.setString(1, cpfCliente);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    ProdutoDTO produtoPedido = new ProdutoDTO();
                    produtoPedido.setNomeProduto(rs.getString("Produto"));
                    produtoPedido.setQuantidade(rs.getInt("Quantidade"));
                    produtoPedido.setNfPedido(rs.getString("NF_Pedido"));
                    produtoPedido.setDataPedido(rs.getDate("Data_Pedido").toLocalDate());

                    produtosPedidos.add(produtoPedido);
                }
            }
        }

        return produtosPedidos;
    }

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

    @Override
    public List<Produto> findMaisVendidos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(FIND_MAIS_VENDIDOS_QUERY);
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
            LOGGER.log(Level.SEVERE, "Erro ao buscar produtos mais vendidos.", ex);
            throw new SQLException("Erro ao buscar produtos mais vendidos.");
        }

        return produtos;
    }

}
