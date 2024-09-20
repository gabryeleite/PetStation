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

import com.ecommerce.petstation.daos.PedidoProdutoDAO;
import com.ecommerce.petstation.models.PedidoProduto;

@Repository
public class PgPedidoProdutoDAO implements PedidoProdutoDAO{

    private final Connection connection;

    private static final Logger LOGGER = Logger.getLogger(PgPedidoDAO.class.getName());

    public PgPedidoProdutoDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(PedidoProduto venda) throws SQLException {
        String sql = "INSERT INTO petstation.pedido_produto(num_pedido, num_produto, qnt_produto, preco_produto) VALUES(?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            //stmt.setInt(1, venda.getId());
            stmt.setInt(1, venda.getNumPedido());
            stmt.setInt(2, venda.getNumProduto());
            stmt.setInt(3, venda.getQntProduto());
            stmt.setBigDecimal(4, venda.getPrecoProduto());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao criar venda.", ex);
            if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao criar venda: campos obrigatórios não podem ser nulos.");
            } else {
                throw new SQLException("Erro ao criar venda.");
            }
        }
    }

    @Override
    public PedidoProduto read(Integer id) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'read'");
    }

    @Override
    public void update(PedidoProduto venda) throws SQLException {
        String sql = "UPDATE petstation.pedido_produto SET qnt_produto = ?, preco_produto = ? WHERE id = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, venda.getQntProduto());
            stmt.setBigDecimal(2, venda.getPrecoProduto());

            if (stmt.executeUpdate() < 1) {
                throw new SQLException("Erro ao editar: venda não encontrada.");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar venda.", ex);
            if (ex.getMessage().equals("Erro ao editar: venda não encontrada.")) {
                throw ex;
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao editar venda: campos obrigatórios não podem ser nulos.");
            } else {
                throw new SQLException("Erro ao editar venda.");
            }
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        // nao faz sentido deletar venda
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public List<PedidoProduto> all() throws SQLException {
        List<PedidoProduto> vendas = new ArrayList<>();
        String sql = "SELECT id, num_pedido, num_produto, qnt_produto, preco_produto FROM petstation.pedido_produto;";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                PedidoProduto pedido = new PedidoProduto();
                pedido.setId(rs.getInt("id"));
                pedido.setNumPedido(rs.getInt("num_pedido"));
                pedido.setNumProduto(rs.getInt("num_produto"));
                pedido.setQntProduto(rs.getInt("qnt_produto"));
                pedido.setPrecoProduto(rs.getBigDecimal("preco_produto"));
                vendas.add(pedido);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao listar vendas.", ex);
            throw new SQLException("Erro ao listar vendas.");
        }
        return vendas;
    }

}
