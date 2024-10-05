package com.ecommerce.petstation.daos.daosPg;

import com.ecommerce.petstation.daos.ClienteDAO;
import com.ecommerce.petstation.dtos.ClientePedidoDTO;
import com.ecommerce.petstation.models.Cliente;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class PgClienteDAO implements ClienteDAO {
    private final Connection connection;

    private static final Logger LOGGER = Logger.getLogger(PgClienteDAO.class.getName());

    public PgClienteDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO petstation.cliente (nome, sobrenome, sexo, data_nascimento, cpf, email, senha) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getSobrenome());
            statement.setString(3, cliente.getSexo());
            statement.setObject(4, cliente.getDataNascimento());
            statement.setString(5, cliente.getCpf());
            statement.setString(6, cliente.getEmail());
            statement.setString(7, cliente.getSenha());
            statement.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao inserir cliente.", ex);
            if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao inserir cliente: campos obrigatórios não podem ser nulos.");
            } else {
                throw new SQLException("Erro ao inserir cliente.");
            }
        }
    }

    @Override
    public Cliente read(Integer id) throws SQLException {
        // read feito por CPF
        // "SELECT nome, sobrenome, sexo, data_nascimento, cpf, email, senha FROM petstation.cliente WHERE cpf = ?;";
        throw new UnsupportedOperationException("Unimplemented method 'read'");
    }


    @Override
    public void update(Cliente cliente) throws SQLException {
        String sql = "UPDATE petstation.cliente SET nome = ?, sobrenome = ?, sexo = ?, data_nascimento = ?, cpf = ?, email = ?, senha = ? WHERE cpf = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getSobrenome());
            statement.setString(3, cliente.getSexo());
            statement.setObject(4, cliente.getDataNascimento());
            statement.setString(5, cliente.getCpf());
            statement.setString(6, cliente.getEmail());
            statement.setString(7, cliente.getSenha());

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao editar: cliente não encontrado.");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar cliente.", ex);
            if (ex.getMessage().equals("Erro ao editar: cliente não encontrado.")) {
                throw ex;
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao editar cliente: campos obrigatórios não podem ser nulos.");
            } else {
                throw new SQLException("Erro ao editar cliente.");
            }
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        // delete feito por CPF
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void delete(String cpf) throws SQLException {
        String sql = "DELETE FROM petstation.cliente WHERE cpf = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cpf);
            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao excluir: cliente não encontrado.");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao excluir cliente.", ex);
            if (ex.getMessage().equals("Erro ao excluir: cliente não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao excluir cliente.");
            }
        }
    }

    @Override
    public List<Cliente> all() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT nome, sobrenome, sexo, data_nascimento, cpf, email, senha FROM petstation.cliente;";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setNome(rs.getString("nome"));
                cliente.setSobrenome(rs.getString("sobrenome"));
                cliente.setSexo(rs.getString("sexo"));
                cliente.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                cliente.setCpf(rs.getString("cpf"));
                cliente.setEmail(rs.getString("email"));
                cliente.setSenha(rs.getString("senha"));
                clientes.add(cliente);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao listar clientes.", ex);
            throw new SQLException("Erro ao listar clientes.");
        }
        return clientes;
    }


    @Override
    public Cliente findByCpfCliente(String cpf) throws SQLException {
        String sql = "SELECT * FROM petstation.cliente WHERE cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setNome(rs.getString("nome"));
                cliente.setSobrenome(rs.getString("sobrenome"));
                cliente.setSexo(rs.getString("sexo"));
                cliente.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                cliente.setCpf(rs.getString("cpf"));
                cliente.setEmail(rs.getString("email"));
                cliente.setSenha(rs.getString("senha"));
                return cliente;
            } else {
                return null;
            }
        }
    }

    public List<ClientePedidoDTO> findClientesMaisAtivos() throws SQLException {
        String sql = "SELECT c.cpf, c.nome, c.sobrenome, COUNT(p.nota_fiscal) AS numero_pedidos " +
                     "FROM petstation.cliente c " +
                     "JOIN petstation.pedido p ON c.cpf = p.cpf_cliente " +
                     "GROUP BY c.cpf, c.nome, c.sobrenome " +
                     "ORDER BY numero_pedidos DESC " +
                     "LIMIT 3";
        
        List<ClientePedidoDTO> clientesAtivos = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ClientePedidoDTO cliente = new ClientePedidoDTO();
                    cliente.setCpf(rs.getString("cpf"));
                    cliente.setNome(rs.getString("nome"));
                    cliente.setSobrenome(rs.getString("sobrenome"));
                    cliente.setNumeroPedidos(rs.getInt("numero_pedidos"));
    
                    clientesAtivos.add(cliente);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar clientes mais ativos.", ex);
            throw new SQLException("Erro ao buscar clientes mais ativos.");
        }
    
        return clientesAtivos;
    }
    

}