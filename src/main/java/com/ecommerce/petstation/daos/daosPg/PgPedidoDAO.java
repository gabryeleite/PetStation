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

import com.ecommerce.petstation.daos.PedidoDAO;
import com.ecommerce.petstation.models.Pedido;

@Repository
public class PgPedidoDAO implements PedidoDAO {
    
    private final Connection connection;

    private static final Logger LOGGER = Logger.getLogger(PgPedidoDAO.class.getName());

    public PgPedidoDAO(Connection connection) {
        this.connection = connection;
    }

    // query filtrar produtos
    /*
     * SELECT 
            c.num_pedido AS "Nº do Pedido",
            c.num_produto AS "Nº do Produto",
            p.nome AS "Produto",
            c.preco_produto AS "Preço",
            c.qnt_produto AS "Quantidade",
            (c.qnt_produto * c.preco_produto) AS "Total"
        FROM 
            petstation.pedido_produto c
        JOIN 
            petstation.produto p ON c.num_produto = p.num
        ORDER BY 
            c.num_pedido;
     */

    @Override
    public void create(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO petstation.pedido(data_pedido, hora_pedido, id_cliente) VALUES(?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            //stmt.setInt(1, pedido.getNum());
            stmt.setObject(1, pedido.getDataPedido());
            stmt.setObject(2, pedido.getHoraPedido());
            stmt.setInt(3, pedido.getIdCliente());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao criar pedido.", ex);
            if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao criar pedido: campos obrigatórios não podem ser nulos.");
            } else {
                throw new SQLException("Erro ao criar pedido.");
            }
        }
    }

    @Override
    public Pedido read(Integer id) throws SQLException {
        Pedido pedido = new Pedido();
        String sql = "SELECT num, data_pedido, hora_pedido, id_cliente FROM petstation.pedido WHERE num = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    pedido.setNum(rs.getInt("num"));
                    pedido.setDataPedido(rs.getDate("data_pedido").toLocalDate());
                    pedido.setHoraPedido(rs.getTime("hora_pedido").toLocalTime());
                    pedido.setIdCliente(rs.getInt("id_cliente"));
                } else {
                    throw new SQLException("Erro ao visualizar: pedido não encontrado.");
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao ler pedido.", ex);
            if (ex.getMessage().equals("Erro ao visualizar: pedido não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao ler pedido.");
            }
        }
        return pedido;
    }

    @Override
    public void update(Pedido pedido) throws SQLException {
        // nao faz sentido atualizar pedido (campos data_pedido, hora_pedido, id_cliente)
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void delete(Integer id) throws SQLException {
        // nao faz sentido deletar pedido
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public List<Pedido> all() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT num, data_pedido, hora_pedido, id_cliente FROM petstation.pedido;";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setNum(rs.getInt("num"));
                pedido.setDataPedido(rs.getDate("data_pedido").toLocalDate());
                pedido.setHoraPedido(rs.getTime("hora_pedido").toLocalTime());
                pedido.setIdCliente(rs.getInt("id_cliente"));
                pedidos.add(pedido);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao listar pedidos.", ex);
            throw new SQLException("Erro ao listar pedidos.");
        }
        return pedidos;
    }

    /* @Override
    public List<Pedido> filtrarPedidos() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(PEDIDOS_QUERY);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Pedido pedido = new Pedido();
                // adicionar Campos

                pedidos.add(pedido);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao filtrar pedidos.", ex);
            throw new SQLException("Erro ao filtrar pedidos.");
        }
        return pedidos;
    } */

}
