package com.ecommerce.petstation.dao;

import com.ecommerce.petstation.models.*;
import com.ecommerce.petstation.models.Cliente;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class PgClienteDAO implements ClienteDAO {
    private final Connection connection;

    private static final Logger LOGGER = Logger.getLogger(PgClienteDAO.class.getName());

    private static final String CREATE_QUERY =
            "INSERT INTO petstation.cliente (nome, sobrenome, sexo, data_nascimento, cpf, email) " +
                    "VALUES (?, ?, ?, ?, ?, ?);";

    private static final String READ_QUERY =
            "SELECT nome, sobrenome, sexo, data_nascimento, cpf, emaipgl FROM petstation.cliente WHERE id_cliente = ?;";

    private static final String UPDATE_QUERY =
            "UPDATE petstation.cliente SET nome = ?, sobrenome = ?, sexo = ?, data_nascimento = ?, cpf = ?, email = ? WHERE id_cliente = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM petstation.cliente WHERE id_cliente = ?;";

    private static final String ALL_QUERY =
            "SELECT id_cliente, nome, sobrenome, sexo, data_nascimento, cpf, email FROM petstation.cliente ORDER BY id_cliente DESC;";

    private static final String FIND_BY_ID_CLIENTE_QUERY =
            "SELECT * FROM petstation.cliente WHERE id_cliente = ?;";

    private static final String CLIENTES_COM_MAIS_PEDIDOS_QUERY =
            "SELECT c.id_cliente, c.nome, c.sobrenome, COUNT(p.num) AS total_pedidos " +
                    "FROM petstation.cliente c " +
                    "JOIN petstation.pedido p ON c.id_cliente = p.id_cliente " +
                    "GROUP BY c.id_cliente, c.nome, c.sobrenome " +
                    "ORDER BY total_pedidos DESC;";

    public PgClienteDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Cliente cliente) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getSobrenome());
            statement.setString(3, cliente.getSexo());
            statement.setObject(4, cliente.getDataNascimento());
            statement.setString(5, cliente.getCpf());
            statement.setString(6, cliente.getEmail());
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
        Cliente cliente = new Cliente();
        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    cliente.setIdCliente(id);
                    cliente.setNome(result.getString("nome"));
                    cliente.setSobrenome(result.getString("sobrenome"));
                    cliente.setSexo(result.getString("sexo"));
                    // Tive que dar um cast pra LocalDate, nao sei se esta correto
                    cliente.setDataNascimento(result.getDate("data_nascimento").toLocalDate());
                    cliente.setCpf(result.getString("cpf"));
                    cliente.setEmail(result.getString("email"));
                } else {
                    throw new SQLException("Erro ao visualizar: cliente não encontrado.");
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao ler cliente.", ex);
            if (ex.getMessage().equals("Erro ao visualizar: cliente não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao ler cliente.");
            }
        }
        return cliente;
    }

    @Override
    public void update(Cliente cliente) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getSobrenome());
            statement.setString(3, cliente.getSexo());
            statement.setObject(4, cliente.getDataNascimento());
            statement.setString(5, cliente.getCpf());
            statement.setString(6, cliente.getEmail());

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
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
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
        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(result.getInt("id_cliente"));
                cliente.setNome(result.getString("nome"));
                cliente.setSobrenome(result.getString("sobrenome"));
                cliente.setSexo(result.getString("sexo"));
                cliente.setDataNascimento(result.getDate("data_nascimento").toLocalDate());
                cliente.setCpf(result.getString("cpf"));
                cliente.setEmail(result.getString("email"));

                clientes.add(cliente);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao listar clientes.", ex);
            throw new SQLException("Erro ao listar clientes.");
        }
        return clientes;
    }

    @Override
    public Cliente findByIdCliente(Integer id_cliente) throws SQLException {
        Cliente cliente = null;
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_CLIENTE_QUERY)) {
            statement.setInt(1, id_cliente);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    cliente = new Cliente();
                    cliente.setNome(result.getString("nome"));
                    cliente.setSobrenome(result.getString("sobrenome"));
                    cliente.setSexo(result.getString("sexo"));
                    // Tive que dar um cast pra LocalDate, nao sei se esta correto
                    cliente.setDataNascimento((LocalDate) result.getObject("data_nascimento"));
                    cliente.setCpf(result.getString("cpf"));
                    cliente.setEmail(result.getString("email"));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar cliente por ID.", ex);
            throw new SQLException("Erro ao buscar cliente por ID.");
        }
        return cliente;
    }

    @Override
    public List<Cliente> findClientesComMaisPedidos() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(CLIENTES_COM_MAIS_PEDIDOS_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(result.getInt("id_cliente"));
                cliente.setNome(result.getString("nome"));
                cliente.setSobrenome(result.getString("sobrenome"));
                clientes.add(cliente);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar clientes com mais pedidos.", ex);
            throw new SQLException("Erro ao buscar clientes com mais pedidos.");
        }
        return clientes;
    }


}