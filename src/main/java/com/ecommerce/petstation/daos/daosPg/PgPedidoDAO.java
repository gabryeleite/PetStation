package com.ecommerce.petstation.daos.daosPg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Repository;

import com.ecommerce.petstation.daos.PedidoDAO;
import com.ecommerce.petstation.dtos.VendasDiaDTO;
import com.ecommerce.petstation.models.Pedido;

@Repository
public class PgPedidoDAO implements PedidoDAO {
    
    private final Connection connection;

    private static final Logger LOGGER = Logger.getLogger(PgPedidoDAO.class.getName());

    public PgPedidoDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO petstation.pedido(nota_fiscal, cpf_cliente, data_pedido, hora_pedido) VALUES(?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, pedido.getNotaFiscal());
            stmt.setString(2, pedido.getCpfCliente());
            stmt.setObject(3, pedido.getDataPedido());
            stmt.setObject(4, pedido.getHoraPedido());
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
        // read feito pela NF
        throw new UnsupportedOperationException("Unimplemented method 'read'");
    }
    
    @Override
    public void update(Pedido pedido) throws SQLException {
        // nao faz sentido atualizar pedido (campos data_pedido, hora_pedido, cpf_cliente)
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
        String sql = "SELECT nota_fiscal, data_pedido, hora_pedido, cpf_cliente FROM petstation.pedido;";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setNotaFiscal(rs.getString("nota_fiscal"));
                pedido.setDataPedido(rs.getDate("data_pedido").toLocalDate());
                pedido.setHoraPedido(rs.getTime("hora_pedido").toLocalTime());
                pedido.setCpfCliente(rs.getString("cpf_cliente"));
                pedidos.add(pedido);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao listar pedidos.", ex);
            throw new SQLException("Erro ao listar pedidos.");
        }
        return pedidos;
    }

    @Override
    public List<Pedido> findByCliente(String cpfCliente) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT nota_fiscal, cpf_cliente, data_pedido, hora_pedido FROM petstation.pedido WHERE cpf_cliente = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cpfCliente);
            try(ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Pedido pedido = new Pedido();
                    pedido.setNotaFiscal(rs.getString("nota_fiscal"));
                    pedido.setCpfCliente(rs.getString("cpf_cliente"));
                    pedido.setDataPedido(rs.getDate("data_pedido").toLocalDate());
                    pedido.setHoraPedido(rs.getTime("hora_pedido").toLocalTime());
                    pedidos.add(pedido);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao listar pedidos.", ex);
            throw new SQLException("Erro ao listar pedidos.");
        }
        return pedidos;
    }

    @Override
    public Pedido findByNF(String nf) throws SQLException {
        Pedido pedido = new Pedido();
        String sql = "SELECT nota_fiscal, data_pedido, hora_pedido, cpf_cliente FROM petstation.pedido WHERE nota_fiscal = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    pedido.setNotaFiscal(rs.getString("nota_fiscal"));
                    pedido.setDataPedido(rs.getDate("data_pedido").toLocalDate());
                    pedido.setHoraPedido(rs.getTime("hora_pedido").toLocalTime());
                    pedido.setCpfCliente(rs.getString("cpf_cliente"));
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

    public List<VendasDiaDTO> findByVendasPorDia(LocalDate inicio, LocalDate fim) throws SQLException {
    String sql = "SELECT p.data_pedido AS Data, SUM(c.qnt_produto) AS QuantidadeTotal " +
                 "FROM petstation.pedido p " +
                 "JOIN petstation.pedido_produto c ON p.nota_fiscal = c.nf_pedido " +
                 "WHERE p.data_pedido BETWEEN ? AND ? " +
                 "GROUP BY p.data_pedido " +
                 "ORDER BY p.data_pedido";

    List<VendasDiaDTO> vendasPorDia = new ArrayList<>();

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setDate(1, java.sql.Date.valueOf(inicio));
        stmt.setDate(2, java.sql.Date.valueOf(fim));

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                VendasDiaDTO venda = new VendasDiaDTO();
                venda.setData(rs.getDate("Data").toLocalDate());
                venda.setQuantidadeTotal(rs.getInt("QuantidadeTotal"));

                vendasPorDia.add(venda);
            }
        }
    } catch (SQLException ex) {
        LOGGER.log(Level.SEVERE, "Erro ao buscar vendas por dia.", ex);
        throw new SQLException("Erro ao buscar vendas por dia.");
    }

    return vendasPorDia;
}


}
